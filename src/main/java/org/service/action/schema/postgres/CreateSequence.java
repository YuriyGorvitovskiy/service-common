package org.service.action.schema.postgres;

import org.service.action.Action;
import org.service.action.IAction;
import org.service.action.Result;

@Action(service = "schema_manager", name = "create_column")
public class CreateSequence implements IAction<CreateSequence.Params, Context> {

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

    @Override
    public Result apply(Params params, Context ctx) {
        String ddl = "CREATE SEQUENCE " + params.schema + "." + params.name
                + " INCREMENT BY 1 "
                + " NO MAXVALUE "
                + " START WITH " + params.start
                + " NO CYCLE";

        ctx.dbc.execute(ddl);

        return Result.empty;

    }
}
