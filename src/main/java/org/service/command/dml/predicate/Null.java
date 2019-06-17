package org.service.command.dml.predicate;

public class Null implements Predicate {
    public final String column;

    public Null(String column) {
        this.column = column;
    }
}
