package org.service.common.sql.schema;

import org.service.common.sql.DataType;
import org.service.common.sql.IColumn;

public class Column implements IColumn {

    /// Set by table constructor.
    Table          table;
    final String   name;
    final DataType type;

    public Column(String name, DataType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DataType getDataType() {
        return type;
    }

    @Override
    public Table getTable() {
        return table;
    }

}
