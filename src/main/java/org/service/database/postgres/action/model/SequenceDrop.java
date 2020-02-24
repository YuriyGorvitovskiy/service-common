package org.service.database.postgres.action.model;

import org.service.action2.Event;
import org.service.action2.Service;
import org.service.action2.model.ISequenceDrop;
import org.service.database.postgres.ServicePostgres;

import io.vavr.collection.List;

@Service(ServicePostgres.NAME)
public class SequenceDrop implements ISequenceDrop<Context> {

    @Override
    public List<Event<?>> apply(Params params, Context ctx) {
        String ddl = "DROP SEQUENCE " + params.schema + "." + params.name + " CASCADE";
        ctx.dbc.execute(ddl);

        return List.empty();
    }
}
