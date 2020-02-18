package org.service.action.schema.postgres;

import org.service.action.Action;
import org.service.action.IAction;
import org.service.action.Result;
import org.service.action.schema.Service;

@Action(service = Service.POSTGRES, name = Service.Drop.TABLE)
public class DropTable implements IAction<DropTable.Params, Context> {

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
        String ddl = "DROP TABLE " + params.schema + "." + params.name + " CASCADE";
        ctx.dbc.execute(ddl);
        return Result.empty;
    }
}
