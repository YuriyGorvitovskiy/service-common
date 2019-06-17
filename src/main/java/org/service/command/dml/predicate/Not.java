package org.service.command.dml.predicate;

public class Not implements Predicate {
    public final Predicate condition;

    public Not(Predicate condition) {
        this.condition = condition;
    }
}
