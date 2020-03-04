package org.service.model.cache;

import org.service.action2.Event;
import org.service.action2.model.ISchemaDrop;

import io.vavr.collection.List;

public class SchemaDrop implements ISchemaDrop<TableInfoCache> {

    final TableDrop tableDrop = new TableDrop();

    @Override
    public List<Event<?>> apply(Params params, TableInfoCache context) {
        context.removeAll(k -> k.schema.equals(params.name));

        return List.empty();
    }

}
