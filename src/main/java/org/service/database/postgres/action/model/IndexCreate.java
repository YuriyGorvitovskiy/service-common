package org.service.database.postgres.action.model;

import java.util.stream.Collectors;

import org.service.action2.Event;
import org.service.action2.Service;
import org.service.action2.model.IIndexCreate;
import org.service.database.postgres.ServicePostgres;

import io.vavr.collection.List;

@Service(ServicePostgres.NAME)
public class IndexCreate implements IIndexCreate<Context> {

    @Override
    public List<Event<?>> apply(Params params, Context ctx) {
        String ddl = params.primary
                ? "ALTER TABLE " + params.schema + "." + params.table + " ADD " + primaryDDL(params)
                : indexDDL(params);

        ctx.dbc.execute(ddl);

        return List.empty();
    }

    public String primaryDDL(Index params) {
        return "CONSTRAINT " + params.name + " PRIMARY KEY (" + params.columns.collect(Collectors.joining(", ")) + ")";
    }

    public String indexDDL(Params params) {
        return "CREATE INDEX " + params.name + " ON " + params.schema + "." + params.table +
                "(" + params.columns.collect(Collectors.joining(", ")) + ")";
    }
}
