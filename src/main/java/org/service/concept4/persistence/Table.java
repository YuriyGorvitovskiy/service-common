package org.service.concept4.persistence;

import org.service.common.sql.schema.Column;

import io.vavr.collection.List;

public class Table {
    public final String       name;
    public final List<Column> columns;
    public final List<Index>  indexes;

    public Table(String name, List<Column> columns, List<Index> indexes) {
        this.name = name;
        this.columns = columns;
        this.indexes = indexes;
    }
}
