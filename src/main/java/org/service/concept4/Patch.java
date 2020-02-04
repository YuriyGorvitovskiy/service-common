package org.service.concept4;

import io.vavr.collection.Map;

public class Patch {

    public enum Operation {
        CREATE,
        UPDATE,
        DELETE
    };

    public final String              type;
    public final String              id;
    public final Operation           op;
    public final Map<String, Object> attr;

    public Patch(Operation op, String type, String id, Map<String, Object> attr) {
        this.type = type;
        this.id = id;
        this.op = op;
        this.attr = attr;
    }
}
