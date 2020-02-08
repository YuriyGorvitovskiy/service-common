package org.service.action.schema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.service.action.Action;
import org.service.action.Counter;
import org.service.action.Equal;
import org.service.action.From;
import org.service.action.IAction;
import org.service.action.Key;
import org.service.action.Result;
import org.service.action.Where;
import org.service.immutable.data.Patch;
import org.service.immutable.data.Patch.Operation;
import org.service.immutable.data.Row;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;

@Action(service = "schema_manager", name = "create_index")
public class CreateIndex implements IAction<CreateIndex.Params, CreateIndex.Context> {

    public static class Params {
        public final String       schema;
        public final String       table;
        public final String       name;
        public final Boolean      primary;
        public final List<String> columns;

        Params(String schema, String table, String name, Boolean primary, List<String> columns) {
            this.schema = schema;
            this.table = table;
            this.name = name;
            this.primary = primary;
            this.columns = columns;
        }
    }

    public static class Column {

        public final Long id;

        Column(Long id) {
            this.id = id;
        }
    }

    public static class Table {

        public final Long                id;

        @From(schema = "model", table = "columns")
        @Where({
                @Equal(column = "table", context = "id"),
        })
        @Key(column = "name")
        public final Map<String, Column> columns;

        Table(Long id, Map<String, Column> columns) {
            this.id = id;
            this.columns = columns;
        }
    }

    public static class Schema {

        public final Long  id;

        @From(schema = "model", table = "tables")
        @Where({
                @Equal(column = "schema", context = "id"),
                @Equal(column = "name", param = "table")
        })
        public final Table table;

        Schema(Long id, Table table) {
            this.id = id;
            this.table = table;
        }
    }

    public static class Context {

        @Counter("index_id")
        public final Supplier<Long> index_id_counter;

        public final Connection     dbc;

        @From(schema = "model", table = "schemas")
        @Where({
                @Equal(column = "name", param = "schema")
        })
        public final Schema         schema;

        Context(Supplier<Long> index_id_counter, Connection dbc, Schema schema) {
            this.index_id_counter = index_id_counter;
            this.dbc = dbc;
            this.schema = schema;
        }

    }

    @Override
    public Result apply(Params params, Context ctx) throws Exception {
        String ddl = params.primary
                ? "ALTER TABLE " + params.schema + "." + params.table +
                        " ADD CONSTRAINT " + params.name + " PRIMARY KEY (" +
                        params.columns.collect(Collectors.joining(", ")) + ")"
                : "CREATE INDEX " + params.name + " ON " + params.schema + "." + params.table +
                        "(" + params.columns.collect(Collectors.joining(", ")) + ")";

        try (PreparedStatement ps = ctx.dbc.prepareStatement(ddl)) {
            ps.execute();
        }
        Long id = ctx.index_id_counter.get();
        return Result.ofPatches(
                List.of(new Patch(Operation.INSERT,
                        Row.of("model",
                                "indexes",
                                new Tuple2<>("id", id),
                                new Tuple2<>("table", ctx.schema.table.id),
                                new Tuple2<>("name", params.name),
                                new Tuple2<>("primary", params.primary)))),
                params.columns.map(c -> new Patch(Operation.INSERT,
                        Row.of("model",
                                "index_columns",
                                new Tuple2<>("id", id),
                                new Tuple2<>("column", ctx.schema.table.columns.get(c).get().id)))));
    }

    public String primaryDDL(Params params) {

    }
}
