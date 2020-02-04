package org.service.immutable.schema;

public class Column {

    public final Table    table;
    public final Long     id;
    public final String   name;
    public final DataType type;

    Column(Table table, Long id, String name, DataType type) {
        this.table = table;
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Column id(Long id) {
        return new Column(null, id, this.name, this.type);
    }

    public Column name(String name) {
        return new Column(null, this.id, name, this.type);
    }

    public Column type(DataType type) {
        return new Column(null, this.id, this.name, type);
    }

    // For fluent definition
    public static Column of() {
        return new Column(null, null, null, null);
    }

    // For hard coded definition
    public static Column of(String name, DataType type) {
        return new Column(null, null, name, type);
    }

    // For programming definition
    public static Column of(Long id, String name, DataType type) {
        return new Column(null, id, name, type);
    }
}
