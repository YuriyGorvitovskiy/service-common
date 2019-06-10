package org.service.common.sql.query;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.service.common.sql.IColumn;
import org.service.common.sql.IPseudoSql;
import org.service.common.sql.ITable;
import org.service.common.sql.schema.Table;

public class TableAlias implements ITable, IPseudoSql {

    final String label;
    final ITable table;

    /// Has to preserve order of columns
    final Map<String, TableAliasColumn> columns = new LinkedHashMap<>();

    public TableAlias(String label, ITable table) {
        this.label = label;
        this.table = table;
        for (IColumn column : table.getColumns()) {
            TableAliasColumn aliasColumn = new TableAliasColumn(column);
            this.columns.put(column.getName(), aliasColumn);
            aliasColumn.table = this;
        }
    }

    @Override
    public String getName() {
        return null == label ? table.getName() : label;
    }

    @Override
    public Collection<TableAliasColumn> getColumns() {
        return columns.values();
    }

    @Override
    public TableAliasColumn getColumn(String name) {
        return columns.get(name);
    }

    @Override
    public String toString() {
        return toPseudoSql("");
    }

    @Override
    public String toPseudoSql(String indent) {
        String sql = null;
        if (table instanceof Table) {
            sql = table.getName();
        } else if (table instanceof IPseudoSql) {
            sql = ((IPseudoSql) table).toPseudoSql(indent);
        }

        if (null == label) {
            return sql;
        }
        return sql + " " + label;
    }

}
