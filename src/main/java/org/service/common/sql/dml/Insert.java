package org.service.common.sql.dml;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.service.common.sql.IPseudoSql;
import org.service.common.sql.query.Literal;
import org.service.common.sql.schema.Column;
import org.service.common.sql.schema.Table;

import com.google.common.collect.Iterables;

public class Insert implements IPseudoSql {

    final Table                              table;
    final Map<String, Pair<Column, Literal>> values = new LinkedHashMap<>();

    public Insert(Table table) {
        this.table = table;
    }

    public Insert value(Column column, Literal value) {
        values.put(column.getName(), Pair.of(column, value));
        return this;
    }

    public Insert value(String columnName, Object value) {
        Column  column  = table.getColumn(columnName);
        Literal literal = new Literal(column.getDataType(), value);
        return value(column, literal);
    }

    public Table getTable() {
        return table;
    }

    public Collection<Pair<Column, Literal>> getValues() {
        return values.values();
    }

    public Literal getValue(String columnName) {
        return values.get(columnName).getValue();
    }

    public Literal getValue(Column column) {
        return values.get(column.getName()).getRight();
    }

    public Column getColumnd(String columnName) {
        return values.get(columnName).getLeft();
    }

    @Override
    public String toPseudoSql(String indent) {
        final String  NEXT_LINE  = "\n" + indent;
        final String  SUB_INDENT = indent + "             ";

        StringBuilder sb         = new StringBuilder();

        sb.append(indent);
        sb.append("INSERT INTO ");
        sb.append(table.getName());

        sb.append(" (");
        sb.append(String.join(", ", Iterables.transform(values.values(), c -> c.getLeft().getName())));
        sb.append(")");

        sb.append(NEXT_LINE);
        sb.append("     VALUES (");
        sb.append(String.join(", ", Iterables.transform(values.values(), c -> c.getRight().toPseudoSql(SUB_INDENT))));
        sb.append(")");

        return sb.toString();
    }

    @Override
    public String toString() {
        return toPseudoSql("");
    }
}
