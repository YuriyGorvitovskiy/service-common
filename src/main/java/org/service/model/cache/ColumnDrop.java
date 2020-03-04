package org.service.model.cache;

import org.service.action2.Event;
import org.service.action2.model.IColumnDrop;

import io.vavr.collection.List;

public class ColumnDrop implements IColumnDrop<TableInfoCache> {

    @Override
    public List<Event<?>> apply(Params params, TableInfoCache context) {
        context.compute(new TableKey(params.schema, params.table), t -> t.removeColumn(params.name));

        return List.empty();
    }

}
