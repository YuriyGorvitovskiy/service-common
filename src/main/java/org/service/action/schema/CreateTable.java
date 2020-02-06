package org.service.action.schema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.function.Supplier;

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

import io.vavr.Tuple2;

@Action(service = "schema_manager", name = "create_table")
public class CreateTable implements IAction<CreateTable.Params, CreateTable.Context> {

    public static class Params {
        public final String schema;
        public final String name;

        Params(String schema, String name) {
            this.schema = schema;
            this.name = name;
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

        public final Connection     dbc;

        @From(schema = "model", table = "schemas")
        @Where({
                @Equal(column = "name", param = "schema")
        })
        public final Schema         schema;

        Context(Supplier<Long> table_id_counter, Connection dbc, Schema schema) {
            this.table_id_counter = table_id_counter;
            this.dbc = dbc;
            this.schema = schema;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) throws Exception {
        String ddl = "CREATE TABLE " + params.schema + "." + params.name + "()";
        try (PreparedStatement ps = ctx.dbc.prepareStatement(ddl)) {
            ps.execute();
        }

        return Result.of(new Patch(Operation.INSERT,
                Row.of("model",
                        "tables",
                        new Tuple2<>("id", ctx.table_id_counter.get()),
                        new Tuple2<>("schema", ctx.schema.id),
                        new Tuple2<>("name", params.name))));

    }
}
