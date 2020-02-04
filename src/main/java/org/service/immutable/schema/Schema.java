package org.service.immutable.schema;

import javax.validation.constraints.NotNull;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;

public class Schema {

    public final Long               id;
    public final String             name;

    public final Map<String, Table> tables;

    Schema(Long id, String name, @NotNull Map<String, Table> tables) {
        this.id = id;
        this.name = name;
        this.tables = tables.mapValues(t -> new Table(this, t.id, t.name, t.columns, t.primary, t.indexes));
    }

    public Schema id(Long id) {
        return new Schema(id, this.name, this.tables);
    }

    public Schema name(String name) {
        return new Schema(this.id, name, this.tables);
    }

    public Schema table(Table table) {
        return new Schema(this.id, this.name, this.tables.put(table.name, table));
    }

    public Schema table(String name) {
        return new Schema(this.id, this.name, this.tables.put(name, Table.of().name(name)));
    }

    public Schema tables(Table... tables) {
        return new Schema(this.id,
                this.name,
                this.tables.merge(Stream.of(tables).collect(HashMap.collector(t -> t.name, t -> t))));
    }

    public Schema tables(Stream<Table> tables) {
        return new Schema(this.id, this.name, this.tables.merge(tables.collect(HashMap.collector(t -> t.name, t -> t))));
    }

    // For fluent definition
    public static Schema of() {
        return new Schema(null, null, HashMap.empty());
    }

    // For hard coded definition
    public static Schema of(String name, Table... tables) {
        return new Schema(null, name, Stream.of(tables).collect(HashMap.collector(t -> t.name, t -> t)));
    }

    // For programming definition
    public static Schema of(Long id, String name, Stream<Table> tables) {
        return new Schema(id, name, tables.collect(HashMap.collector(t -> t.name, t -> t)));
    }
}
