package org.service.concept;

import java.util.HashMap;
import java.util.Map;

public class Patch {

    public enum Operation {
        UPSERT,
        UPDATE,
        DELETE
    }

    public final Operation              operation;

    public final Batch                  batch;

    public final Map<Attribute, Object> attributes = new HashMap<>();

    public Patch(Operation operation, Batch batch) {
        this.operation = operation;
        this.batch     = batch;
    }
}
