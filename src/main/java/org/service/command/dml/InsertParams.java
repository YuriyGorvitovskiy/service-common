package org.service.concept.db.event;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class RequestInsert {
    public final String                       table;
    public final ImmutableMap<String, Object> values;

    public RequestInsert(String table, Map<String, Object> values) {
        this.table = table;
        this.values = ImmutableMap.copyOf(values);
    }
}
