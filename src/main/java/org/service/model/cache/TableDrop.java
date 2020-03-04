package org.service.model.cache;

import org.service.action2.Event;
import org.service.action2.model.ITableDrop;

import io.vavr.collection.List;

public class TableDrop implements ITableDrop<TableInfoCache> {

    @Override
    public List<Event<?>> apply(Params params, TableInfoCache context) {
        context.remove(new TableKey(params.schema, params.name));

        return List.empty();
    }

}
