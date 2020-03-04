package org.service.model.cache;

import java.util.Objects;

public class TableKey {

    public final String schema;

    public final String table;

    public TableKey(String schema, String table) {
        this.schema = schema;
        this.table = table;
    }

    @Override
    public int hashCode() {
        return Objects.hash(schema, table);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TableKey)) {
            return false;
        }
        TableKey other = (TableKey) o;
        return Objects.equals(this.schema, other.schema) &&
                Objects.equals(this.table, other.table);
    }
}
