package org.service.model.cache;

import org.service.immutable.schema.DataType;

import io.vavr.collection.HashSet;
import io.vavr.collection.Map;
import io.vavr.collection.Set;

public class TableInfo {

    public final Map<String, DataType> columnsType;

    public final String      primaryName;
    public final Set<String> primaryColumns;

    public TableInfo(Map<String, DataType> columnsType, String primaryName, Set<String> primaryColumns) {
        this.columnsType = columnsType;
        this.primaryName = primaryName;
        this.primaryColumns = primaryColumns;
    }

    public TableInfo setPrimary(String primaryName, Iterable<String> primaryColumns) {
        return new TableInfo(this.columnsType, primaryName, HashSet.ofAll(primaryColumns));
    }

    public TableInfo removePrimary() {
        return new TableInfo(this.columnsType, null, HashSet.empty());
    }

    public TableInfo addColumn(String column, DataType type) {
        return new TableInfo(this.columnsType.put(column, type), primaryName, primaryColumns);
    }

    public TableInfo removeColumn(String column) {
        return new TableInfo(this.columnsType.remove(column), primaryName, primaryColumns);
    }
}
