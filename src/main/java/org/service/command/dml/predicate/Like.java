package org.service.command.dml.predicate;

public class Like implements Predicate {
    public final String column;
    public final Object value;

    public Like(String column, Object value) {
        this.column = column;
        this.value = value;
    }

}
