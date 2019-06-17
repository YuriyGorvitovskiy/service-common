package org.service.command.dml.predicate;

public class More implements Predicate {
    public final String column;
    public final Object value;

    public More(String column, Object value) {
        this.column = column;
        this.value = value;
    }

}
