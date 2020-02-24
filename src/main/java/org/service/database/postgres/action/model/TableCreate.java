package org.service.database.postgres.action.model;

import java.util.stream.Collectors;

import org.service.action2.Event;
import org.service.action2.Service;
import org.service.action2.model.IIndexCreate;
import org.service.action2.model.IIndexCreate.Index;
import org.service.action2.model.ITableCreate;
import org.service.database.postgres.ServicePostgres;

import io.vavr.collection.List;

@Service(ServicePostgres.NAME)
public class TableCreate implements ITableCreate<Context> {

    final ColumnCreate columnCreate = new ColumnCreate();
    final IndexCreate  indexCreate  = new IndexCreate();

    @Override
    public List<Event<?>> apply(Params params, Context ctx) {
        String ddl = "CREATE TABLE " + params.schema + "." + params.name + "(" +
                params.columns
                    .map(c -> columnCreate.columnDDL(c))
                    .appendAll(null == params.primary
                            ? List.empty()
                            : List.of(params.primary).map(i -> indexCreate.primaryDDL(i)))
                    .collect(Collectors.joining(", "))
                + ")";
        ctx.dbc.execute(ddl);

        for (Index index : params.indexes) {
            IIndexCreate.Params indexParams = new IIndexCreate.Params(params.schema, params.name, false, index);
            indexCreate.apply(indexParams, ctx);
        }

        return List.empty();
    }
}
