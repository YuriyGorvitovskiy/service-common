package org.service.database.postgres.action.model;

import org.service.action2.Event;
import org.service.action2.Service;
import org.service.action2.model.ISchemaCreate;
import org.service.action2.model.ISequenceCreate;
import org.service.action2.model.ITableCreate;
import org.service.database.postgres.ServicePostgres;

import io.vavr.collection.List;

@Service(ServicePostgres.NAME)
public class SchemaCreate implements ISchemaCreate<Context> {

    final TableCreate    tableAction    = new TableCreate();
    final SequenceCreate sequenceAction = new SequenceCreate();

    @Override
    public List<Event<?>> apply(Params params, Context ctx) {
        String ddl = "CREATE SCHEMA " + params.name;
        ctx.dbc.execute(ddl);

        for (ITableCreate.Table table : params.tables) {
            ITableCreate.Params tableParams = new ITableCreate.Params(params.name, table);
            tableAction.apply(tableParams, ctx);
        }

        for (ISequenceCreate.Sequence sequence : params.sequences) {
            ISequenceCreate.Params sequenceParams = new ISequenceCreate.Params(params.name, sequence);
            sequenceAction.apply(sequenceParams, ctx);
        }

        return List.empty();
    }
}
