package org.service.command.dml;

import io.vavr.collection.Map;

public class InsertParams implements DMLParams {
    public final String         table;
    public final Map<String, ?> values;

    public InsertParams(String table, Map<String, ?> values) {
        this.table = table;
        this.values = values;
    }
}
