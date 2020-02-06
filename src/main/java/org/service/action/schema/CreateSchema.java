package org.service.action.schema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.function.Supplier;

import org.service.action.Action;
import org.service.action.Counter;
import org.service.action.IAction;
import org.service.action.Result;
import org.service.immutable.data.Patch;
import org.service.immutable.data.Patch.Operation;
import org.service.immutable.data.Row;

import io.vavr.Tuple2;

@Action(service = "schema_manager", name = "create_schema")
public class CreateSchema implements IAction<CreateSchema.Params, CreateSchema.Context> {

    public static class Params {
        public final String name;

        Params(String name) {
            this.name = name;
        }
    }

    public static class Context {

        @Counter("schema_id")
        public final Supplier<Long> schema_id_counter;

        public final Connection     dbc;

        Context(Supplier<Long> schema_id_counter, Connection dbc) {
            this.schema_id_counter = schema_id_counter;
            this.dbc = dbc;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) throws Exception {
        String ddl = "CREATE SCHEMA " + params.name;
        try (PreparedStatement ps = ctx.dbc.prepareStatement(ddl)) {
            ps.execute();
        }

        return Result.of(new Patch(Operation.INSERT,
                Row.of("model",
                        "schemas",
                        new Tuple2<>("id", ctx.schema_id_counter.get()),
                        new Tuple2<>("name", params.name))));
    }
}
