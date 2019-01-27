package org.service.common.sql.schema;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Schema {

    final String name;

    final Map<String, Table> tables = new HashMap<>();

    public Schema(String name, Table... tables) {
        this.name = name;
        for (Table table : tables) {
            this.tables.put(table.name, table);
            table.schema = this;
        }
    }

    public String getName() {
        return name;
    }

    public Collection<Table> getTables() {
        return tables.values();
    }

    public Table getTable(String name) {
        return tables.get(name);
    }
}
