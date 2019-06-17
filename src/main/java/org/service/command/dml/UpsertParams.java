package org.service.command.dml;

import io.vavr.collection.Map;

public class UpsertParams implements DMLParams {
    public final String              table;
    public final Map<String, Object> keys;
    public final Map<String, Object> values;

    public UpsertParams(String table, Map<String, Object> keys, Map<String, Object> values) {
        this.table = table;
        this.keys = keys;
        this.values = values;
    }
}
