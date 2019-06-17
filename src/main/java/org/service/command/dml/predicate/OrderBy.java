package org.service.concept.db.event;

public class Sorting {
    public final String  column;
    public final boolean ascending;

    public Sorting(String column, boolean ascending) {
        this.column = column;
        this.ascending = ascending;
    }
}
