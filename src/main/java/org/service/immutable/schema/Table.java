package org.service.immutable.schema;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;

public class Table {

    public final Schema              schema;

    public final Long                id;
    public final String              name;

    public final List<Column>        columns;
    public final Map<String, Column> columnMap;

    public final Index               primary;

    public final List<Index>         indexes;
    public final Map<String, Index>  indexMap;

    Table(Schema schema,
          Long id,
          String name,
          List<Column> columns,
          Index primary,
          List<Index> indexes) {
        this.schema = schema;

        this.id = id;
        this.name = name;

        this.columns = columns.map(c -> new Column(this, c.id, c.name, c.type)).collect(List.collector());
        this.columnMap = this.columns.collect(HashMap.collector(c -> c.name, c -> c));

        this.primary = null == primary ? null : new Index(this, true, primary.id, primary.name, primary.columnNames);

        this.indexes = indexes.map(i -> new Index(this, false, i.id, i.name, i.columnNames));
        this.indexMap = indexes.collect(HashMap.collector(i -> i.name, i -> i));
    }

    public Table id(Long id) {
        return new Table(null,
                id,
                this.name,
                this.columns,
                this.primary,
                this.indexes);
    }

    public Table name(String name) {
        return new Table(null,
                this.id,
                name,
                this.columns,
                this.primary,
                this.indexes);
    }

    public Table column(Column column) {
        return new Table(null,
                this.id,
                this.name,
                this.columns.append(column),
                this.primary,
                this.indexes);
    }

    public Table column(String name, DataType type) {
        return new Table(null,
                this.id,
                this.name,
                this.columns.append(Column.of(name, type)),
                this.primary,
                this.indexes);
    }

    public Table columns(Column... column) {
        return new Table(null,
                this.id,
                this.name,
                this.columns.appendAll(Stream.of(column)),
                this.primary,
                this.indexes);
    }

    public Table columns(Stream<Column> column) {
        return new Table(null,
                this.id,
                this.name,
                this.columns.appendAll(column),
                this.primary,
                this.indexes);
    }

    public Table primary(Index primary) {
        return new Table(null,
                this.id,
                this.name,
                this.columns,
                primary,
                this.indexes);
    }

    public Table primary(String name, String... columnNames) {
        return new Table(null,
                this.id,
                this.name,
                this.columns,
                Index.of(name, columnNames),
                this.indexes);
    }

    public Table primary(Long id, String name, Stream<String> columnNames) {
        return new Table(null,
                this.id,
                this.name,
                this.columns,
                Index.of(id, name, columnNames),
                this.indexes);
    }

    public Table index(Index index) {
        return new Table(null,
                this.id,
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
                this.id,
                this.name,
                this.columns,
                this.primary,
                this.indexes.append(Index.of(name, columnNames)));
    }

    public Table index(Long id, String name, Stream<String> columnNames) {
        return new Table(null,
                this.id,
                this.name,
                this.columns,
                this.primary,
                this.indexes.append(Index.of(id, name, columnNames)));
    }

    public Table indexes(Index... index) {
        return new Table(null,
                this.id,
                this.name,
                this.columns,
                this.primary,
                this.indexes.appendAll(Stream.of(index)));
    }

    public Table indexes(Stream<Index> index) {
        return new Table(null,
                this.id,
                this.name,
                this.columns,
                this.primary,
                this.indexes.appendAll(index));
    }

    // For fluent definition
    public static Table of() {
        return new Table(null, null, null, List.empty(), null, List.empty());
    }

    // For hard coded definition
    public static Table of(String name, Stream<Column> columns, Index primary, Stream<Index> indexes) {
        return new Table(null, null, name, columns.collect(List.collector()), primary, indexes.collect(List.collector()));
    }

    // For programming definition
    public static Table of(Long id, String name, Stream<Column> columns, Index primary, Stream<Index> indexes) {
        return new Table(null, id, name, columns.collect(List.collector()), primary, indexes.collect(List.collector()));
    }

}
