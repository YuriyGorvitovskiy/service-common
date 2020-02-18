package org.service.action.schema.postgres;

import org.service.action.Action;
import org.service.action.IAction;
import org.service.action.Result;
import org.service.action.schema.Service;
import org.service.immutable.schema.DataType;

@Action(service = Service.POSTGRES, name = Service.Create.COLUMN)
public class CreateColumn implements IAction<CreateColumn.Params, Context> {

    public static class Params {
        public final String   schema;
        public final String   table;
        public final String   name;
        public final DataType type;

        public Params(String schema, String table, String name, DataType type) {
            this.schema = schema;
            this.table = table;
            this.name = name;
            this.type = type;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        String ddl = "ALTER TABLE " + params.schema + "." + params.table +
                " ADD COLUMN " + columnDDL(params);
        ctx.dbc.execute(ddl);
        return Result.empty;
    }

    public String columnDDL(Params params) {
        return params.name + " " + params.type.postgres();
    }
}
