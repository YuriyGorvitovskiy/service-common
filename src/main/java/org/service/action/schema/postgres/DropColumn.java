package org.service.action.schema.postgres;

import org.service.action.Action;
import org.service.action.IAction;
import org.service.action.Result;

@Action(service = "postgres", name = "drop_column")
public class DropColumn implements IAction<DropColumn.Params, Context> {

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

    @Override
    public Result apply(Params params, Context ctx) {
        String ddl = "ALTER TABLE " + params.schema + "." + params.table +
                " DROP COLUMN " + params.name + " CASCADE";
        ctx.dbc.execute(ddl);

        return Result.empty;
    }
}
