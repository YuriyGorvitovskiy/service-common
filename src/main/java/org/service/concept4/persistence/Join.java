package org.service.concept4.persistence;

import io.vavr.Tuple2;
import io.vavr.collection.Map;

public class Join {
    public enum Kind {
        FROM,
        INNER,
        LEFT,
        RIGHT,
        FULL,
        CROSS
    };

    public final Kind                                kind;

    public final String                              table;

    public final Map<String, Tuple2<String, String>> columnMatchTableColumn;

}
