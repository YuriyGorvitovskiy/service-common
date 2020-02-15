package org.service.action.schema.combine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Supplier;

import org.service.action.Action;
import org.service.action.Equal;
import org.service.action.From;
import org.service.action.IAction;
import org.service.action.Result;
import org.service.action.Sequence;
import org.service.action.Where;
import org.service.immutable.data.Patch;
import org.service.immutable.data.Patch.Operation;
import org.service.immutable.data.Row;

import io.vavr.Tuple2;

@Action(service = "schema_manager", name = "create_column")
public class CreateSequence implements IAction<CreateSequence.Params, CreateSequence.Context> {

    public static class Params {
        public final String schema;
        public final String name;
        public final Long   start;

        Params(String schema, String name, Long start) {
            this.schema = schema;
            this.name = name;
            this.start = start;
        }
    }

    public static class Schema {

        public final Long id;

        Schema(Long id) {
            this.id = id;
        }
    }

    public static class Context {

        @Sequence("sequence_id")
        public final Supplier<Long> seq_sequence_id;

        public final Connection     dbc;

        @From(schema = "model", table = "schemas")
        @Where({ @Equal(column = "name", param = "schema") })
        public final Schema         schema;

        Context(Supplier<Long> seq_sequence_id, Connection dbc, Schema schema) {
            this.seq_sequence_id = seq_sequence_id;
            this.dbc = dbc;
            this.schema = schema;
        }

    }

    @Override
    public Result apply(Params params, Context ctx) {
        String ddl = "CREATE SEQUENCE " + params.schema + "." + params.name
                + " INCREMENT BY 1 "
                + " NO MAXVALUE "
                + " START WITH " + params.start
                + " NO CYCLE";
        try (PreparedStatement ps = ctx.dbc.prepareStatement(ddl)) {
            ps.execute();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return Result.of(new Patch(Operation.insert,
                Row.of("model",
                        "sequences",
                        new Tuple2<>("id", ctx.seq_sequence_id.get()),
                        new Tuple2<>("schema", ctx.schema.id),
                        new Tuple2<>("name", params.name))));

    }
}
