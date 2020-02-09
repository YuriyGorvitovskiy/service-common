package org.service.action.schema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.service.action.Action;
import org.service.action.Counter;
import org.service.action.Equal;
import org.service.action.From;
import org.service.action.IAction;
import org.service.action.Result;
import org.service.action.Where;
import org.service.immutable.data.Patch;
import org.service.immutable.data.Patch.Operation;
import org.service.immutable.data.Row;
import org.service.immutable.schema.DataType;

import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;

@Action(service = "schema_manager", name = "create_table")
public class CreateTable implements IAction<CreateTable.Params, CreateTable.Context> {

    public static class Column {
        public final String   name;
        public final DataType type;

        Column(String name, DataType type) {
            this.name = name;
            this.type = type;
        }
    }

    public static class Index {
        public final String       name;
        public final List<String> columns;

        Index(String name, List<String> columns) {
            this.name = name;
            this.columns = columns;
        }
    }

    public static class Params {
        public final String       schema;
        public final String       name;

        public final List<Column> columns;
        public final Index        primary;
        public final List<Index>  indexes;

        Params(String schema, String name, List<Column> columns, Index primary, List<Index> indexes) {
            this.schema = schema;
            this.name = name;
            this.columns = columns;
            this.primary = primary;
            this.indexes = indexes;
        }
    }

    public static class Schema {

        public final Long id;

        Schema(Long id) {
            this.id = id;
        }
    }

    public static class Context {

        @Counter("table_id")
        public final Supplier<Long> table_id_counter;

        @Counter("column_id")
        public final Supplier<Long> column_id_counter;

        @Counter("index_id")
        public final Supplier<Long> index_id_counter;

        public final Connection     dbc;

        @From(schema = "model", table = "schemas")
        @Where({ @Equal(column = "name", param = "schema") })
        public final Schema         schema;

        Context(Supplier<Long> table_id_counter,
                Supplier<Long> column_id_counter,
                Supplier<Long> index_id_counter,
                Connection dbc,
                Schema schema) {
            this.table_id_counter = table_id_counter;
            this.column_id_counter = column_id_counter;
            this.index_id_counter = index_id_counter;
            this.dbc = dbc;
            this.schema = schema;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        CreateColumn createColumn = new CreateColumn();
        CreateIndex  createIndex  = new CreateIndex();
        String       ddl          = "CREATE TABLE " + params.schema + "." + params.name + "(" +
                params.columns
                    .map(c -> createColumn.columnDDL(new CreateColumn.Params(params.schema, params.name, c.name, c.type)))
                    .appendAll(null == params.primary
                            ? List.empty()
                            : List.of(params.primary)
                                .map(p -> createIndex
                                    .primaryDDL(new CreateIndex.Params(params.schema, params.name, p.name, true, p.columns))))
                    .collect(Collectors.joining(", "));
        try (PreparedStatement ps = ctx.dbc.prepareStatement(ddl)) {
            ps.execute();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        for (Index index : params.indexes) {
            CreateIndex.Params indexParams = new CreateIndex.Params(
                    params.schema,
                    params.name,
                    index.name,
                    false,
                    index.columns);
            try (PreparedStatement ps = ctx.dbc.prepareStatement(createIndex.indexDDL(indexParams))) {
                ps.execute();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        Long                            table_id      = ctx.table_id_counter.get();

        List<Patch>                     columnPatches = params.columns.map(c -> createColumn.patch(
                new CreateColumn.Params(params.schema, params.name, c.name, c.type),
                new CreateColumn.Context(ctx.column_id_counter,
                        ctx.dbc,
                        new CreateColumn.Schema(ctx.schema.id,
                                new CreateColumn.Table(table_id)))));

        Map<String, CreateIndex.Column> columnIds     = columnPatches.collect(HashMap.collector(
                p -> p.row.asString("name"),
                p -> new CreateIndex.Column(p.row.asLong("id"))));

        return Result.ofPatches(
                List.of(new Patch(Operation.insert,
                        Row.of("model",
                                "tables",
                                new Tuple2<>("id", table_id),
                                new Tuple2<>("schema", ctx.schema.id),
                                new Tuple2<>("name", params.name)))),
                params.columns.map(c -> createColumn.patch(
                        new CreateColumn.Params(params.schema, params.name, c.name, c.type),
                        new CreateColumn.Context(ctx.column_id_counter,
                                ctx.dbc,
                                new CreateColumn.Schema(ctx.schema.id,
                                        new CreateColumn.Table(table_id))))),
                null == params.primary
                        ? List.empty()
                        : createIndex.patches(
                                new CreateIndex.Params(params.schema,
                                        params.name,
                                        params.primary.name,
                                        true,
                                        params.primary.columns),
                                new CreateIndex.Context(ctx.index_id_counter,
                                        ctx.dbc,
                                        new CreateIndex.Schema(ctx.schema.id, new CreateIndex.Table(table_id, columnIds)))),
                params.indexes.flatMap(i -> createIndex.patches(
                        new CreateIndex.Params(params.schema, params.name, i.name, false, i.columns),
                        new CreateIndex.Context(ctx.index_id_counter,
                                ctx.dbc,
                                new CreateIndex.Schema(ctx.schema.id, new CreateIndex.Table(table_id, columnIds))))));
    }

}
