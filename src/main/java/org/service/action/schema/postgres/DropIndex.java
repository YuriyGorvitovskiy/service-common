package org.service.action.schema.postgres;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.service.action.Action;
import org.service.action.IAction;
import org.service.action.Result;

@Action(service = "schema_manager", name = "drop_index")
public class DropIndex implements IAction<DropIndex.Params, Context> {

    public static class Params {
        public final String schema;
        public final String name;

        Params(String schema, String table, String name) {
            this.schema = schema;
            this.name = name;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        String pkTable = null;

        String sql = "SELECT table_name "
                + "FROM information_schema.table_constraints "
                + "WHERE table_schema = ? AND constraint_name = ?";
        try (PreparedStatement ps = ctx.dbc.prepareStatement(sql)) {
            ps.setString(1, params.schema);
            ps.setString(2, params.name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                pkTable = rs.getString(1);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        String ddl = null != pkTable
                ? "ALTER TABLE " + params.schema + "." + pkTable + " DROP CONSTRAINT " + params.name
                : "DROP INDEX " + params.schema + "." + params.name + " CASCASE";

        try (PreparedStatement ps = ctx.dbc.prepareStatement(ddl)) {
            ps.execute();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return Result.empty;
    }
}
