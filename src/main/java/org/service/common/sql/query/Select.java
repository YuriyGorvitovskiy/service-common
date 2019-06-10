package org.service.common.sql.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.service.common.sql.IPseudoSql;
import org.service.common.sql.ITable;

import com.google.common.collect.Iterables;

public class Select implements ITable, IPseudoSql {

    /// Has to preserve order of columns
    final Map<String, SelectColumn> columns = new LinkedHashMap<>();
    final List<Join>                joins   = new ArrayList<>();
    final List<Sorting>             sorting = new ArrayList<>();
    ICondition                      where   = null;

    public Select() {
    }

    public Select column(TableAliasColumn aliasColumn, String label) {
        SelectColumn column = new SelectColumn(label, aliasColumn);
        this.columns.put(column.getName(), column);
        column.select = this;
        return this;
    }

    public Select column(TableAliasColumn aliasColumn) {
        SelectColumn column = new SelectColumn(null, aliasColumn);
        this.columns.put(column.getName(), column);
        column.select = this;
        return this;
    }

    public Select from(TableAlias table) {
        joins.add(new Join(Join.Kind.FROM, table, null));
        return this;
    }

    public Select inner(TableAlias table, ICondition condition) {
        joins.add(new Join(Join.Kind.INNER, table, condition));
        return this;
    }

    public Select left(TableAlias table, ICondition condition) {
        joins.add(new Join(Join.Kind.LEFT, table, condition));
        return this;
    }

    public Select right(TableAlias table, ICondition condition) {
        joins.add(new Join(Join.Kind.RIGHT, table, condition));
        return this;
    }

    public Select orderBy(IComparable column, Sorting.Order ascending) {
        sorting.add(new Sorting(column, ascending));
        return this;
    }

    public Select where(ICondition condition) {
        where = condition;
        return this;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Collection<SelectColumn> getColumns() {
        return columns.values();
    }

    @Override
    public SelectColumn getColumn(String name) {
        return columns.get(name);
    }

    @Override
    public String toString() {
        return toPseudoSql("");
    }

    @Override
    public String toPseudoSql(String indent) {
        final String NEXT_LINE = "\n" + indent;
        final String SUB_INDENT = indent + "       ";

        StringBuilder sb = new StringBuilder();

        sb.append(indent);
        sb.append("SELECT ");

        sb.append(String.join(", ", Iterables.transform(columns.values(), c -> c.toPseudoSql(SUB_INDENT))));

        if (!joins.isEmpty()) {
            sb.append(NEXT_LINE);
            sb.append(String.join(NEXT_LINE, Iterables.transform(joins, (j) -> j.toPseudoSql(SUB_INDENT))));
        }

        if (null != where) {
            sb.append(NEXT_LINE);
            sb.append(" WHERE ");
            sb.append(where.toPseudoSql(SUB_INDENT));
        }

        if (!sorting.isEmpty()) {
            sb.append(NEXT_LINE);
            sb.append(" ORDER BY ");
            sb.append(String.join(", ", Iterables.transform(sorting, (s) -> s.toPseudoSql(SUB_INDENT))));
        }

        return sb.toString();
    }

}
