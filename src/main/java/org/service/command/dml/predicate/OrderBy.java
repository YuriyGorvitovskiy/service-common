package org.service.command.dml.predicate;

public class OrderBy {
    public final String  column;
    public final boolean ascending;

    public OrderBy(String column, boolean ascending) {
        this.column = column;
        this.ascending = ascending;
    }
}
