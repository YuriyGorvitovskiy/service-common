package org.service.action2.model.postgres;

import org.service.action2.Event;
import org.service.action2.Service;
import org.service.action2.model.ISequenceCreate;

import io.vavr.collection.List;

@Service(Postgres.SERVICE)
public class SequenceCreate implements ISequenceCreate<Context> {

    @Override
    public List<Event<?>> apply(Params params, Context ctx) {
        String ddl = "CREATE SEQUENCE " + params.schema + "." + params.name
                + " INCREMENT BY 1 "
                + " NO MAXVALUE "
                + " START WITH " + params.start
                + " NO CYCLE";

        ctx.dbc.execute(ddl);

        return List.empty();
    }
}
