package org.service.action.schema.original;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

@Action(service = "schema_manager", name = "drop_table")
public class DropSequence implements IAction<DropSequence.Params, DropSequence.Context> {

    public static class Params {
        public final String schema;
        public final String name;

        Params(String schema, String name) {
            this.schema = schema;
            this.name = name;
        }
    }

    public static class Sequence {

        public final Long id;

        Sequence(Long id) {
            this.id = id;
        }
    }

    public static class Schema {

        public final Long     id;

        @From(schema = "model", table = "sequences")
        @Where({
                @Equal(column = "schema", context = "id"),
                @Equal(column = "name", param = "name")
        })
        public final Sequence sequence;

        Schema(Long id, Sequence sequence) {
            this.id = id;
            this.sequence = sequence;
        }
    }

    public static class Context {

        public final Connection dbc;

        @From(schema = "model", table = "schemas")
        @Where({ @Equal(column = "name", param = "schema") })
        public final Schema     schema;

        Context(Connection dbc, Schema schema) {
            this.dbc = dbc;
            this.schema = schema;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        String ddl = "DROP SEQUENCE " + params.schema + "." + params.name + " CASCADE";
        try (PreparedStatement ps = ctx.dbc.prepareStatement(ddl)) {
            ps.execute();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return Result.of(new Patch(Operation.delete, Row.of("model", "sequences", new Tuple2<>("id", ctx.schema.sequence.id))));
    }
}
