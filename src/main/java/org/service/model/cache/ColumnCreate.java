package org.service.model.cache;

import org.service.action2.Event;
import org.service.action2.model.IColumnCreate;

import io.vavr.collection.List;

public class ColumnCreate implements IColumnCreate<TableInfoCache> {

    @Override
    public List<Event<?>> apply(Params params, TableInfoCache context) {
        context.compute(new TableKey(params.schema, params.table), t -> t.addColumn(params.name, params.type));

        return List.empty();
    }

}
