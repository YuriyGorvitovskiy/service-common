package org.service.action.schema.postgres;

import java.util.stream.Collectors;

import org.service.action.Action;
import org.service.action.IAction;
import org.service.action.Result;
import org.service.action.schema.Service;

import io.vavr.collection.List;

@Action(service = Service.POSTGRES, name = Service.Create.INDEX)
public class CreateIndex implements IAction<CreateIndex.Params, Context> {

    public static class Params {
        public final String       schema;
        public final String       table;
        public final String       name;
        public final Boolean      primary;
        public final List<String> columns;

        public Params(String schema, String table, String name, Boolean primary, List<String> columns) {
            this.schema = schema;
            this.table = table;
            this.name = name;
            this.primary = primary;
            this.columns = columns;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        String ddl = params.primary
                ? "ALTER TABLE " + params.schema + "." + params.table + " ADD " + primaryDDL(params)
                : indexDDL(params);

        ctx.dbc.execute(ddl);

        return Result.empty;
    }

    public String primaryDDL(Params params) {
        return "CONSTRAINT " + params.name + " PRIMARY KEY (" + params.columns.collect(Collectors.joining(", ")) + ")";
    }

    public String indexDDL(Params params) {
        return "CREATE INDEX " + params.name + " ON " + params.schema + "." + params.table +
                "(" + params.columns.collect(Collectors.joining(", ")) + ")";
    }
}
