package org.service.common.sql.query;

import org.service.common.sql.DataType;
import org.service.common.sql.IColumn;
import org.service.common.sql.IPseudoSql;

public class SelectColumn implements IColumn, IPseudoSql {

    Select            select;
    final String      label;
    final IComparable original;

    public SelectColumn(String label, TableAliasColumn original) {
        this.label = label;
        this.original = original;
    }

    @Override
    public String getName() {
        if (null != label) {
            return label;
        }
        if (original instanceof IColumn) {
            return ((IColumn) original).getName();
        }
        return null;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public Select getTable() {
        return select;
    }

    @Override
    public DataType getDataType() {
        return original.getDataType();
    }

    public IComparable getOriginal() {
        return original;
    }

    @Override
    public String toString() {
        return toPseudoSql("");
    }

    @Override
    public String toPseudoSql(String indent) {
        String sql = original.toPseudoSql(indent);
        if (null == label) {
            return sql;
        }
        return sql + " AS " + label;
    }
}
