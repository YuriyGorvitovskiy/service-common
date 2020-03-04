package org.service.model.cache;

import org.service.action2.Event;
import org.service.action2.model.ITableCreate;

import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;

public class TableCreate implements ITableCreate<TableInfoCache> {

    @Override
    public List<Event<?>> apply(Params params, TableInfoCache context) {
        TableInfo info = new TableInfo(
                params.columns.map(c -> new Tuple2<>(c.name, c.type)).collect(HashMap.collector()),
                null == params.primary ? null : params.primary.name,
                null == params.primary ? HashSet.empty() : HashSet.ofAll(params.primary.columns));

        context.put(new TableKey(params.schema, params.name), info);

        return List.empty();
    }

}
