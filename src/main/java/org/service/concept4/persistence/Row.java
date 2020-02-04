package org.service.concept4.persistence;

import io.vavr.collection.Map;

public class Row {
    public final String              table;

    public final Map<String, Object> columns;

    public Row(String table, Map<String, Object> columns) {
        this.table = table;
        this.columns = columns;
    }

}
