package org.service.concept4;

import io.vavr.collection.Map;

public class Event {

    public final String              op;

    public final Map<String, Object> params;

    public Event(String op, Map<String, Object> params) {
        this.op = op;
        this.params = params;
    }
}
