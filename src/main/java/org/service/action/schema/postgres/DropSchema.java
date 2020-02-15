package org.service.action.schema.postgres;

import org.service.action.Action;
import org.service.action.IAction;
import org.service.action.Result;

import io.vavr.collection.List;

@Action(service = "schema_manager", name = "drop_schema")
public class DropSchema implements IAction<DropSchema.Params, Context> {

    public static class Params {
        public final String name;

        Params(String name) {
            this.name = name;
        }
    }

    DropTable action = new DropTable();

    @Override
    public Result apply(Params params, Context ctx) {
        // Schema could contains a lot of tables,
        // In this case DROP SCHEMA could exceed transaction limit.
        // So let us drop all tables individually.
        List<String> tables = getTables(params, ctx);
        for (String table : tables) {
            action.apply(new DropTable.Params(params.name, table), ctx);
        }

        String ddl = "DROP SCHEMA " + params.name + " CASCADE";
        ctx.dbc.execute(ddl);

        return Result.empty;
    }

    public List<String> getTables(Params params, Context ctx) {
        String sql = "SELECT table_name "
                + "FROM information_schema.tables "
                + "WHERE table_schema = ?";

        List<String> tables = ctx.dbc.executeQuery(
                sql,
                ps -> {
                    ps.setString(1, params.name);
                },
                rs -> rs.getString(1));
        return tables;
    }
}
