package org.service.action.schema;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.service.action.Action;
import org.service.action.Equal;
import org.service.action.From;
import org.service.action.IAction;
import org.service.action.Result;
import org.service.action.Where;
import org.service.immutable.data.Patch;
import org.service.immutable.data.Patch.Operation;
import org.service.immutable.data.Row;

import io.vavr.Tuple2;
import io.vavr.collection.List;

@Action(service = "schema_manager", name = "drop_schema")
public class DropSchema implements IAction<DropSchema.Params, DropSchema.Context> {

    public static class Params {
        public final String name;

        Params(String name) {
            this.name = name;
        }
    }

    public static class Column {

        public final Long id;

        Column(Long id) {
            this.id = id;
        }
    }

    public static class Index {

        public final Long id;

        Index(Long id) {
            this.id = id;
        }
    }

    public static class Table {

        public final Long         id;

        public final String       name;

        @From(schema = "model", table = "columns")
        @Where({
                @Equal(column = "table", context = "id"),
        })
        public final List<Column> columns;

        @From(schema = "model", table = "indexes")
        @Where({
                @Equal(column = "table", context = "id"),
        })
        public final List<Index>  indexes;

        Table(Long id, String name, List<Column> columns, List<Index> indexes) {
            this.id = id;
            this.name = name;
            this.columns = columns;
            this.indexes = indexes;
        }
    }

    public static class Schema {

        public final Long        id;

        @From(schema = "model", table = "tables")
        @Where({
                @Equal(column = "schema", context = "id"),
        })
        public final List<Table> tables;

        Schema(Long id, List<Table> tables) {
            this.id = id;
            this.tables = tables;
        }
    }

    public static class Context {

        public final Connection dbc;

        @From(schema = "model", table = "schemas")
        @Where({
                @Equal(column = "name", param = "schema")
        })
        public final Schema     schema;

        Context(Connection dbc, Schema schema) {
            this.dbc = dbc;
            this.schema = schema;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) throws Exception {
        List<Table> tables = ctx.schema.tables;
        for (Table t : tables) {
            String ddl = "DROP TABLE " + t.name + " CASCADE";
            try (PreparedStatement ps = ctx.dbc.prepareStatement(ddl)) {
                ps.execute();
            }
        }

        String ddl = "DROP SCHEMA " + params.name + " CASCADE";
        try (PreparedStatement ps = ctx.dbc.prepareStatement(ddl)) {
            ps.execute();
        }

        List<Index>  indexes = ctx.schema.tables.flatMap(t -> t.indexes);
        List<Column> columns = ctx.schema.tables.flatMap(t -> t.columns);
        Long         id      = ctx.schema.id;
        return Result.ofPatches(
                indexes.map(i -> new Patch(Operation.DELETE, Row.of("model", "index_columns", new Tuple2<>("id", i)))),
                indexes.map(i -> new Patch(Operation.DELETE, Row.of("model", "indexes", new Tuple2<>("id", i)))),
                columns.map(c -> new Patch(Operation.DELETE, Row.of("model", "columns", new Tuple2<>("id", c)))),
                tables.map(t -> new Patch(Operation.DELETE, Row.of("model", "tables", new Tuple2<>("id", t)))),
                List.of(new Patch(Operation.DELETE, Row.of("model", "schemas", new Tuple2<>("id", id)))));
    }
}
