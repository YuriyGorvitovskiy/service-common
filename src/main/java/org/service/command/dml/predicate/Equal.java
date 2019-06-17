package org.service.command.dml.predicate;

public class Equal implements Predicate {
    public final String column;
    public final Object value;

    public Equal(String column, Object value) {
        this.column = column;
        this.value = value;
    }
}
