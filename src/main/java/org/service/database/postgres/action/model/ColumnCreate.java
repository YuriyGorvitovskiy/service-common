package org.service.database.postgres.action.model;

import org.service.action.schema.postgres.Context;
import org.service.action2.Event;
import org.service.action2.Service;
import org.service.action2.model.IColumnCreate;
import org.service.database.postgres.ServicePostgres;

import io.vavr.collection.List;

@Service(ServicePostgres.NAME)
public class ColumnCreate implements IColumnCreate<Context> {

    @Override
    public List<Event<?>> apply(Params params, Context ctx) {
        String ddl = "ALTER TABLE " + params.schema + "." + params.table +
                " ADD COLUMN " + columnDDL(params);

        ctx.dbc.execute(ddl);

        return List.empty();
    }

    public String columnDDL(Column params) {
        return params.name + " " + params.type.postgres();
    }

}
