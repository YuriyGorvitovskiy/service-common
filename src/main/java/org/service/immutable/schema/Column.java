package org.service.immutable.schema;

public class Column {

    public final Table    table;
    public final String   name;
    public final DataType type;

    Column(Table table, String name, DataType type) {
        this.table = table;
        this.name = name;
        this.type = type;
    }

    public Column name(String name) {
        return new Column(null, name, this.type);
    }

    public Column type(DataType type) {
        return new Column(null, this.name, type);
    }

    public static Column of() {
        return new Column(null, null, null);
    }

    public static Column of(String name) {
        return new Column(null, name, null);
    }

    public static Column of(String name, DataType type) {
        return new Column(null, name, type);
    }

    /*
    public static class Builder implements Function<Table, Column> {
    
        String   name;
    
        DataType type;
    
        Builder() {
        }
    
        public Builder name(String name) {
            this.name = name;
            return this;
        }
    
        public Builder type(DataType type) {
            this.type = type;
            return this;
        }
    
        @Override
        public Column apply(Table table) {
            return new Column(table, name, type);
        }
    }
    
    public static Builder of() {
        return new Builder();
    }
    
    public static Builder of(String name) {
        return of().name(name);
    }
    
    public static Builder of(String name, DataType type) {
        return of().name(name).type(type);
    }
     */
}
