package org.service.common.sql.query;

import org.service.common.sql.DataType;
import org.service.common.sql.IColumn;

public class TableAliasColumn implements IColumn, IComparable {

    TableAlias    table;
    final IColumn original;

    public TableAliasColumn(IColumn orginal) {
        this.original = orginal;
    }

    @Override
    public String getName() {
        return original.getName();
    }

    @Override
    public TableAlias getTable() {
        return table;
    }

    @Override
    public DataType getDataType() {
        return original.getDataType();
    }

    @Override
    public String toString() {
        return toPseudoSql("");
    }

    @Override
    public String toPseudoSql(String indent) {
        return table.getName() + "." + original.getName();
    }
}
