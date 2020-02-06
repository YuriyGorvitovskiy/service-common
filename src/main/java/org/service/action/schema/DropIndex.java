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

@Action(service = "schema_manager", name = "drop_index")
public class DropIndex implements IAction<DropIndex.Params, DropIndex.Context> {

    public static class Params {
        public final String schema;
        public final String table;
        public final String name;

        Params(String schema, String table, String name) {
            this.schema = schema;
            this.table = table;
            this.name = name;
        }
    }

    public static class Index {

        public final Long    id;
        public final Boolean primary;

        Index(Long id, Boolean primary) {
            this.id = id;
            this.primary = primary;
        }
    }

    public static class Table {

        public final Long  id;

        @From(schema = "model", table = "indexes")
        @Where({
                @Equal(column = "table", context = "id"),
                @Equal(column = "name", param = "name")
        })
        public final Index index;

        Table(Long id, Index index) {
            this.id = id;
            this.index = index;
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
        String ddl = ctx.schema.table.index.primary
                ? "ALTER TABLE " + params.schema + "." + params.table +
                        " DROP CONSTRAINT " + params.name
                : "DROP INDEX " + params.schema + "." + params.name;

        try (PreparedStatement ps = ctx.dbc.prepareStatement(ddl)) {
            ps.execute();
        }
        return Result.of(
                new Patch(Operation.DELETE, Row.of("model", "indexes", new Tuple2<>("id", ctx.schema.table.index.id))),
                new Patch(Operation.DELETE, Row.of("model", "index_columns", new Tuple2<>("id", ctx.schema.table.index.id))));
    }
}
