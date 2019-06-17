package org.service.command.dml.predicate;

public class Less implements Predicate {
    public final String column;
    public final Object value;

    public Less(String column, Object value) {
        this.column = column;
        this.value = value;
    }

}
