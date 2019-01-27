package org.service.common.sql.schema;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.service.common.sql.ITable;

public class Table implements ITable {
    /// Set by Schema constructor;
    Schema schema;

    final String name;

    /// Has to preserve order of columns
    final Map<String, Column> columns = new LinkedHashMap<>();

    public Table(String name, Column... columns) {
        this.name = name;
        for (Column column : columns) {
            this.columns.put(column.name, column);
            column.table = this;
        }
    }

    public Schema getSchema() {
        return schema;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<Column> getColumns() {
        return columns.values();
    }

    @Override
    public Column getColumn(String name) {
        return columns.get(name);
    }
}
