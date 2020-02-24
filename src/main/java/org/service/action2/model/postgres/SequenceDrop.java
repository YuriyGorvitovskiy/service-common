package org.service.action2.model.postgres;

import org.service.action2.Event;
import org.service.action2.Service;
import org.service.action2.model.ISequenceDrop;

import io.vavr.collection.List;

@Service(Postgres.SERVICE)
public class SequenceDrop implements ISequenceDrop<Context> {

    @Override
    public List<Event<?>> apply(Params params, Context ctx) {
        String ddl = "DROP SEQUENCE " + params.schema + "." + params.name + " CASCADE";
        ctx.dbc.execute(ddl);

        return List.empty();
    }
}
