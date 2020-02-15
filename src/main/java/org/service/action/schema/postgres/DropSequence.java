package org.service.action.schema.postgres;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.service.action.Action;
import org.service.action.IAction;
import org.service.action.Result;

@Action(service = "schema_manager", name = "drop_table")
public class DropSequence implements IAction<DropSequence.Params, Context> {

    public static class Params {
        public final String schema;
        public final String name;

        Params(String schema, String name) {
            this.schema = schema;
            this.name = name;
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

        return Result.empty;
    }
}
