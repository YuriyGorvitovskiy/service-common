package org.service.immutable.schema;

import io.vavr.collection.List;
import io.vavr.collection.Stream;

public class Index {
    // Derived from table
    public final Table        table;
    public final boolean      primary;
    public final List<Column> columns;

    public final Long         id;
    public final String       name;
    public final List<String> columnNames;

    Index(Table table,
          boolean primary,
          Long id,
          String name,
          List<String> columnNames) {
        this.table = table;
        this.primary = primary;
        this.id = id;
        this.name = name;
        this.columnNames = columnNames;
        this.columns = null == table
                ? null
                : this.columnNames.map(n -> table.columnMap.get(n).get());
    }

    public Index id(Long id) {
        return new Index(null,
                false,
                id,
                this.name,
                this.columnNames);
    }

    public Index name(String name) {
        return new Index(null,
                false,
                this.id,
                name,
                this.columnNames);
    }

    public Index column(String columnName) {
        return new Index(null,
                false,
                this.id,
                this.name,
                this.columnNames.append(columnName));
    }

    public Index columns(String... columnNames) {
        return new Index(null,
                false,
                this.id,
                this.name,
                this.columnNames.appendAll(Stream.of(columnNames)));
    }

    public Index columns(Stream<String> columnNames) {
        return new Index(null,
                false,
                this.id,
                this.name,
                this.columnNames.appendAll(columnNames));
    }

    public static Index of() {
        return new Index(null, false, null, null, List.empty());
    }

    public static Index of(String name, String... columns) {
        return new Index(null, false, null, name, List.of(columns));
    }

    public static Index of(Long id, String name, Stream<String> columns) {
        return new Index(null, false, id, name, columns.collect(List.collector()));
    }
}
