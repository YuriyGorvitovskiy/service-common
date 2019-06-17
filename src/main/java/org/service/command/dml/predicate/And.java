package org.service.command.dml.predicate;

import io.vavr.collection.Seq;

public class And implements Predicate {
    public final Seq<Predicate> conditions;

    public And(Seq<Predicate> conditions) {
        this.conditions = conditions;
    }
}
