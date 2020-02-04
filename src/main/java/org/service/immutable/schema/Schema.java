package org.service.immutable.schema;

import javax.validation.constraints.NotNull;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;

public class Schema {

    public final String             name;

    @NotNull
    public final Map<String, Table> tables;

    Schema(String name, @NotNull Map<String, Table> tables) {
        this.name = name;
        this.tables = tables.mapValues(t -> new Table(this, t.name, t.columns, t.primary, t.indexes));
    }

    public Schema name(String name) {
        return new Schema(name, this.tables);
    }

    public Schema table(Table table) {
        return new Schema(this.name, this.tables.put(table.name, table));
    }

    public Schema table(String name) {
        return new Schema(this.name, this.tables.put(name, Table.of(name)));
    }

    public Schema tables(Table... tables) {
        return new Schema(this.name, this.tables.merge(Stream.of(tables).collect(HashMap.collector(t -> t.name, t -> t))));
    }

    public Schema tables(Stream<Table> tables) {
        return new Schema(this.name, this.tables.merge(tables.collect(HashMap.collector(t -> t.name, t -> t))));
    }

    public static Schema of() {
        return new Schema(null, HashMap.empty());
    }

    public static Schema of(String name) {
        return new Schema(name, HashMap.empty());
    }

    public static Schema of(String name, Table... tables) {
        return new Schema(name, Stream.of(tables).collect(HashMap.collector(t -> t.name, t -> t)));
    }

    public static Schema of(String name, Stream<Table> tables) {
        return new Schema(name, tables.collect(HashMap.collector(t -> t.name, t -> t)));
    }

    /*
    public Schema table(Table table)
    
    public static class Builder implements Supplier<Schema> {
    
        String                name;
    
        Stream<Table.Builder> tables = Stream.empty();
    
        Builder() {
        }
    
        public Builder name(String name) {
            this.name = name;
            return this;
        }
    
        public Builder table(Table.Builder table) {
            tables = tables.append(table);
            return this;
        }
    
        public Builder tables(Table.Builder... table) {
            tables = tables.appendAll(Stream.of(table));
            return this;
        }
    
        public Builder tables(Stream<Table.Builder> table) {
            tables = tables.appendAll(table);
            return this;
        }
    
        @Override
        public Schema get() {
            return new Schema(name, tables);
        }
    }
    
    public static Builder of() {
        return new Builder();
    }
    
    public static Builder of(String name) {
        return of().name(name);
    }
    
    public static Builder of(String name, Table.Builder... tables) {
        return of().name(name).tables(tables);
    }
    
    public static Builder of(String name, Stream<Table.Builder> tables) {
        return of().name(name).tables(tables);
    }
    */
}
