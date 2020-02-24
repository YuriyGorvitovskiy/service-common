package org.service.action2.model.postgres;

import org.service.action.schema.postgres.Context;
import org.service.action2.Event;
import org.service.action2.Service;
import org.service.action2.model.IColumnDrop;

import io.vavr.collection.List;

@Service(Postgres.SERVICE)
public class ColumnDrop implements IColumnDrop<Context> {

    @Override
    public List<Event<?>> apply(Params params, Context ctx) {
        String ddl = "ALTER TABLE " + params.schema + "." + params.table +
                " DROP COLUMN " + params.name + " CASCADE";
        ctx.dbc.execute(ddl);

        return List.empty();
    }

}
