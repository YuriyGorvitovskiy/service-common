package org.service.model.cache;

import org.service.action2.Event;
import org.service.action2.model.ISchemaCreate;
import org.service.action2.model.ITableCreate;

import io.vavr.collection.List;

public class SchemaCreate implements ISchemaCreate<TableInfoCache> {

    final TableCreate tableCreate = new TableCreate();

    @Override
    public List<Event<?>> apply(Params params, TableInfoCache context) {
        params.tables.forEach(t -> tableCreate.apply(new ITableCreate.Params(params.name, t), context));

        return List.empty();
    }

}
