package org.service.immutable.schema;

import io.vavr.collection.List;
import io.vavr.collection.Stream;

public class Index {
    // Derived from table
    public final Table        table;
    public final boolean      primary;
    public final List<Column> columns;

    public final String       name;
    public final List<String> columnNames;

    Index(Table table,
          boolean primary,
          String name,
          List<String> columnNames) {
        this.table = table;
        this.primary = primary;
        this.name = name;
        this.columnNames = columnNames;
        this.columns = null == table
                ? null
                : this.columnNames.map(n -> table.columnMap.get(n).get());
    }

    Index name(String name) {
        return new Index(null,
                false,
                name,
                this.columnNames);
    }

    Index column(String columnName) {
        return new Index(null,
                false,
                this.name,
                this.columnNames.append(columnName));
    }

    Index columns(String... columnNames) {
        return new Index(null,
                false,
                this.name,
                this.columnNames.appendAll(Stream.of(columnNames)));
    }

    Index columns(Stream<String> columnNames) {
        return new Index(null,
                false,
                this.name,
                this.columnNames.appendAll(columnNames));
    }

    public static Index of() {
        return new Index(null, false, null, List.empty());
    }

    public static Index of(String name) {
        return new Index(null, false, name, List.empty());
    }

    public static Index of(String name, String... columns) {
        return new Index(null, false, name, List.of(columns));
    }

    public static Index of(String name, Stream<String> columns) {
        return new Index(null, false, name, columns.collect(List.collector()));
    }

    /*
    public static class Builder implements Function<Table, Index> {
    
        String         name;
    
        boolean        primary = false;
    
        Stream<String> columns = Stream.empty();
    
        Builder() {
        }
    
        public Builder name(String name) {
            this.name = name;
            return this;
        }
    
        public Builder primary() {
            primary = true;
            return this;
        }
    
        public Builder column(String name) {
            columns = columns.append(name);
            return this;
        }
    
        public Builder columns(String... names) {
            columns = columns.appendAll(Stream.of(names));
            return this;
        }
    
        @Override
        public Index apply(Table table) {
            return new Index(table, name, primary, columns);
        }
    }
    
    public static Builder of() {
        return new Builder();
    }
    
    public static Builder primary(String name, String... columns) {
        return of().name(name).primary().columns(columns);
    }
    
    public static Builder of(String name, String... columns) {
        return of().name(name).columns(columns);
    }
    
    
    */
}
