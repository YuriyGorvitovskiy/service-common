package org.service.action.schema.postgres;

import org.service.action.Action;
import org.service.action.IAction;
import org.service.action.Result;
import org.service.action.schema.Service;

@Action(service = Service.POSTGRES, name = Service.Drop.INDEX)
public class DropIndex implements IAction<DropIndex.Params, Context> {

    public static class Params {
        public final String schema;
        public final String name;

        public Params(String schema, String name) {
            this.schema = schema;
            this.name = name;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        String sql = "SELECT table_name "
                + "FROM information_schema.table_constraints "
                + "WHERE table_schema = ? AND constraint_name = ?";

        String pkTable = ctx.dbc.executeSingle(
                sql,
                ps -> {
                    ps.setString(1, params.schema);
                    ps.setString(2, params.name);
                },
                rs -> rs.getString(1));

        String ddl = null != pkTable
                ? "ALTER TABLE " + params.schema + "." + pkTable + " DROP CONSTRAINT " + params.name
                : "DROP INDEX " + params.schema + "." + params.name + " CASCASE";

        ctx.dbc.execute(ddl);

        return Result.empty;
    }
}
