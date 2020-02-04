package org.service.concept4.persistence;

import io.vavr.collection.List;

public class Index {

    public final String       name;
    public final boolean      primary;
    public final List<Column> columns;

    public Index(String name, boolean primary, List<Column> columns) {
        this.name = name;
        this.primary = primary;
        this.columns = columns;
    }
}
