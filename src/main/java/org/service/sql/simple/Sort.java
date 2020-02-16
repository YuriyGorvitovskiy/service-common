package org.service.sql.simple;

public class Sort {

    public final String  column;
    public final boolean asc;

    public Sort(String column, boolean asc) {
        this.column = column;
        this.asc = asc;
    }

    public String sql() {
        return column + (asc ? " ASC" : " DESC");
    }
}
