package org.service.concept.db.event;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class RequestMerge {
    public final String                       table;
    public final ImmutableMap<String, Object> keys;
    public final ImmutableMap<String, Object> values;

    public RequestMerge(String table, Map<String, Object> keys, Map<String, Object> values) {
        this.table = table;
        this.keys = ImmutableMap.copyOf(keys);
        this.values = ImmutableMap.copyOf(values);
    }
}
