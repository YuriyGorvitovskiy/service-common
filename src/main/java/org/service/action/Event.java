package org.service.action;

import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;

public class Event {
    public final String         service;
    public final String         action;
    public final Map<String, ?> params;

    Event(String service, String action, Map<String, ?> params) {
        this.service = service;
        this.action = action;
        this.params = params;
    }

    @SafeVarargs
    public static Event of(String service,
                           String action,
                           Tuple2<String, ?>... params) {
        return new Event(service, action, HashMap.ofEntries(params));
    }

    public static Event of(String service,
                           String action,
                           Map<String, ?> params) {
        return new Event(service, action, params);
    }

}
