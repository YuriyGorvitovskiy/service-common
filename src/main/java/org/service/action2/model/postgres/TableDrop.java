package org.service.action2.model.postgres;

import org.service.action2.Event;
import org.service.action2.Service;
import org.service.action2.model.ITableDrop;

import io.vavr.collection.List;

@Service(Postgres.SERVICE)
public class TableDrop implements ITableDrop<Context> {

    final ColumnCreate columnCreate = new ColumnCreate();
    final IndexCreate  indexCreate  = new IndexCreate();

    @Override
    public List<Event<?>> apply(Params params, Context ctx) {
        String ddl = "DROP TABLE " + params.schema + "." + params.name + " CASCADE";
        ctx.dbc.execute(ddl);

        return List.empty();
    }
}
