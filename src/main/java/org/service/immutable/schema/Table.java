package org.service.immutable.schema;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;

public class Table {

    public final Schema              schema;

    public final String              name;

    public final List<Column>        columns;

    public final Map<String, Column> columnMap;

    public final Index               primary;

    public final List<Index>         indexes;

    public final Map<String, Index>  indexMap;

    Table(Schema schema,
          String name,
          List<Column> columns,
          Index primary,
          List<Index> indexes) {
        this.schema = schema;
        this.name = name;

        this.columns = columns.map(c -> new Column(this, c.name, c.type)).collect(List.collector());
        this.columnMap = this.columns.collect(HashMap.collector(c -> c.name, c -> c));

        this.primary = null == primary ? null : new Index(this, true, primary.name, primary.columnNames);

        this.indexes = indexes.map(i -> new Index(this, false, i.name, i.columnNames));
        this.indexMap = indexes.collect(HashMap.collector(i -> i.name, i -> i));
    }

    public Table name(String name) {
        return new Table(null,
                name,
                this.columns,
                this.primary,
                this.indexes);
    }

    public Table column(Column column) {
        return new Table(null,
                this.name,
                this.columns.append(column),
                this.primary,
                this.indexes);
    }

    public Table column(String name, DataType type) {
        return new Table(null,
                this.name,
                this.columns.append(Column.of(name, type)),
                this.primary,
                this.indexes);
    }

    public Table columns(Column... column) {
        return new Table(null,
                this.name,
                this.columns.appendAll(Stream.of(column)),
                this.primary,
                this.indexes);
    }

    public Table columns(Stream<Column> column) {
        return new Table(null,
                this.name,
                this.columns.appendAll(column),
                this.primary,
                this.indexes);
    }

    public Table primary(Index primary) {
        return new Table(null,
                this.name,
                this.columns,
                primary,
                this.indexes);
    }

    public Table primary(String name, String... columnNames) {
        return new Table(null,
                this.name,
                this.columns,
                Index.of(name, columnNames),
                this.indexes);
    }

    public Table primary(String name, Stream<String> columnNames) {
        return new Table(null,
                this.name,
                this.columns,
                Index.of(name, columnNames),
                this.indexes);
    }

    public Table index(Index index) {
        return new Table(null,
                this.name,
                this.columns,
                this.primary,
                this.indexes.append(index));
    }

    public Table index(boolean primary, Index index) {
        return primary ? primary(index) : index(index);
    }

    public Table index(boolean primary, String name) {
        return primary ? primary(name) : index(name);
    }

    public Table index(String name, String... columnNames) {
        return new Table(null,
                this.name,
                this.columns,
                this.primary,
                this.indexes.append(Index.of(name, columnNames)));
    }

    public Table index(String name, Stream<String> columnNames) {
        return new Table(null,
                this.name,
                this.columns,
                this.primary,
                this.indexes.append(Index.of(name, columnNames)));
    }

    public Table indexes(Index... index) {
        return new Table(null,
                this.name,
                this.columns,
                this.primary,
                this.indexes.appendAll(Stream.of(index)));
    }

    public Table indexes(Stream<Index> index) {
        return new Table(null,
                this.name,
                this.columns,
                this.primary,
                this.indexes.appendAll(index));
    }

    public static Table of() {
        return new Table(null, null, List.empty(), null, List.empty());
    }

    public static Table of(String name) {
        return new Table(null, name, List.empty(), null, List.empty());
    }

    public static Table of(String name, Stream<Column> columns, Index primary, Stream<Index> indexes) {
        return new Table(null, name, columns.collect(List.collector()), primary, indexes.collect(List.collector()));
    }

    /*
    public static class Builder implements Function<Schema, Table> {
    
        String                 name;
    
        Stream<Column.Builder> columns = Stream.empty();
    
        Stream<Index.Builder>  indexes = Stream.empty();
    
        Builder() {
        }
    
        public Builder name(String name) {
            this.name = name;
            return this;
        }
    
        public Builder column(String name, DataType type) {
            return column(Column.of().name(name).type(type));
        }
    
        public Builder column(Column.Builder column) {
            columns = columns.append(column);
            return this;
        }
    
        public Builder columns(Column.Builder... column) {
            columns = columns.appendAll(Stream.of(column));
            return this;
        }
    
        public Builder columns(Stream<Column.Builder> column) {
            columns = columns.appendAll(column);
            return this;
        }
    
        public Builder primary(String name, String... columns) {
            return index(Index.of().name(name).primary().columns(columns));
        }
    
        public Builder index(String name, String... columns) {
            return index(Index.of().name(name).columns(columns));
        }
    
        public Builder index(Index.Builder index) {
            indexes = indexes.append(index);
            return this;
        }
    
        public Builder indexes(Index.Builder... index) {
            indexes = indexes.appendAll(Stream.of(index));
            return this;
        }
    
        public Builder indexes(Stream<Index.Builder> index) {
            indexes = indexes.appendAll(index);
            return this;
        }
    
        @Override
        public Table apply(Schema schema) {
            return new Table(schema, name, columns, indexes);
        }
    }
    
    public static Builder of() {
        return new Builder();
    }
    
    public static Builder of(String name) {
        return of().name(name);
    }
    
    public static Builder of(String name, Stream<Column.Builder> columns, Stream<Index.Builder> indexes) {
        return of().name(name).columns(columns).indexes(indexes);
    }
    
    */
}
