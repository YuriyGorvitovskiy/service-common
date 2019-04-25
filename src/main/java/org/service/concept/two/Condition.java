package org.service.concept.two;

import java.util.Collection;

public interface Condition {
    public enum Operation {
        EQUAL,
        NOT_EQUAL,
        IN,
        NOT_IN,
        LIKE,
        NOT_LIKE,
        LESS_THEN,
        NOT_LESS,
        MORE_THEN,
        NOT_MORE,
        BETWEEN,
        OUTSIDE,
        EMPTY,
        NOT_EMPTY,
    }

    Operation getOperation();

    Collection<Object> getValues();
}
