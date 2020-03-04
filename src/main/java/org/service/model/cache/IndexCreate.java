package org.service.model.cache;

import org.service.action2.Event;
import org.service.action2.model.IIndexCreate;

import io.vavr.collection.List;

public class IndexCreate implements IIndexCreate<TableInfoCache> {

    @Override
    public List<Event<?>> apply(Params params, TableInfoCache context) {
        if (params.primary) {
            context.compute(new TableKey(params.schema, params.table), t -> t.setPrimary(params.name, params.columns));
        }
        return List.empty();
    }

}
