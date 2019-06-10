package org.service.concept;

import java.util.List;

public class Filter {

    public static enum Comparison {
        EQUAL,
        NOT_EQUAL,
        IN,
        NOT_IN,
        CONTAINS,
        NOT_CONTAINS,
        LESS_THEN,
        NOT_LESS,
        MORE_THEN,
        NOT_MORE,
        BETWEEN,
        OUTSIDE,
        EMPTY,
        NOT_EMPTY,
    }

    public final Attribute    attribute;
    public final Comparison   comparison;
    public final List<Object> values;

    public Filter(Attribute attribute, Comparison comparison, List<Object> values) {
        this.attribute = attribute;
        this.comparison = comparison;
        this.values = values;
    }
}
