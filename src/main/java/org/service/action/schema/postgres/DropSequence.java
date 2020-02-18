package org.service.action.schema.postgres;

import org.service.action.Action;
import org.service.action.IAction;
import org.service.action.Result;
import org.service.action.schema.Service;

@Action(service = Service.POSTGRES, name = Service.Drop.SEQUENCE)
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
        ctx.dbc.execute(ddl);

        return Result.empty;
    }
}
