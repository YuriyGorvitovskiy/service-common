package org.service.command.dml.predicate;

import io.vavr.collection.Seq;

public class Or implements Predicate {
    public final Seq<Predicate> conditions;

    public Or(Seq<Predicate> conditions) {
        this.conditions = conditions;
    }
}
