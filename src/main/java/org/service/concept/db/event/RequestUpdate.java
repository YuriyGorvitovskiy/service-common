package org.service.concept.db.event;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class RequestUpdate {
    public final String                       table;
    public final ImmutableMap<String, Object> values;
    public final Condition                    condition;

    public RequestUpdate(String table, Map<String, Object> values, Condition condition) {
        this.table = table;
        this.values = ImmutableMap.copyOf(values);
        this.condition = condition;
    }
}
