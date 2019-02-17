package org.service.concept;

import java.util.HashMap;
import java.util.Map;

public class Patch<ID> {

    public enum Operation {
        INSERT,
        UPSERT,
        UPDATE,
        DELETE
    }

    public final Operation           operation;

    public final Model               type;

    public final ID                  id;

    public final Map<String, Object> attributes = new HashMap<>();

    public Patch(Operation operation, Model type, ID id) {
        this.operation = operation;
        this.type      = type;
        this.id        = id;
    }
}
