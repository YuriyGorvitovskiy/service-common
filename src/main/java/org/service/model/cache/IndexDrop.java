package org.service.model.cache;

import org.service.action2.Event;
import org.service.action2.model.IIndexDrop;

import io.vavr.collection.List;

public class IndexDrop implements IIndexDrop<TableInfoCache> {

    @Override
    public List<Event<?>> apply(Params params, TableInfoCache context) {
        TableKey  key  = new TableKey(params.schema, params.table);
        TableInfo info = context.get(key);
        if (params.name.equals(context.get(key).primaryName)) {
            context.put(key, info.removePrimary());
        }
        return List.empty();
    }

}
