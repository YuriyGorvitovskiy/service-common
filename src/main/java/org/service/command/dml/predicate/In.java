package org.service.command.dml.predicate;

import io.vavr.collection.Seq;

public class In implements Predicate {
    public final String column;
    public final Seq<?> values;

    public In(String column, Seq<?> values) {
        this.column = column;
        this.values = values;
    }

}
