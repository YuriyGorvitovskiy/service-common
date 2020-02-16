package org.service.sql.simple;

import java.util.stream.Collectors;

import org.service.dbc.DBStatementBinder;

import io.vavr.collection.List;

public class Select {
    public final List<String>    columns;
    public final String          schema;
    public final String          table;
    public final List<Condition> where;
    public final List<Sort>      orderBy;

    public Select(String... columns) {
        this(List.of(columns), null, null, List.empty(), List.empty());
    }

    public Select(List<String> columns,
                  String schema,
                  String table,
                  List<Condition> where,
                  List<Sort> orderBy) {
        this.columns = columns;
        this.schema = schema;
        this.table = table;
        this.where = where;
        this.orderBy = orderBy;
    }

    public Select from(String schema, String table) {
        return new Select(this.columns, schema, table, this.where, this.orderBy);
    }

    public Select where(Condition... condition) {
        return new Select(
                this.columns,
                this.schema,
                this.table,
                this.where.appendAll(List.of(condition)),
                this.orderBy);
    }

    public Select where(List<Condition> condition) {
        return new Select(
                this.columns,
                this.schema,
                this.table,
                this.where.appendAll(condition),
                this.orderBy);
    }

    public Select orderBy(String column, boolean asc) {
        return new Select(this.columns,
                this.schema,
                this.table,
                this.where,
                this.orderBy.append(new Sort(column, asc)));
    }

    public Select orderBy(String... columns) {
        return new Select(this.columns,
                this.schema,
                this.table,
                this.where,
                this.orderBy.appendAll(List.of(columns).map(c -> new Sort(c, true))));
    }

    public Select orderBy(List<Sort> columns) {
        return new Select(this.columns,
                this.schema,
                this.table,
                this.where,
                this.orderBy.appendAll(columns));
    }

    public String sql() {
        return "SELECT " + columns.collect(Collectors.joining(", ")) +
                "\n  FROM " + schema + "." + table +
                (where.isEmpty() ? "" : "\n WHERE " + where.map(Condition::sql).collect(Collectors.joining(" AND "))) +
                (orderBy.isEmpty() ? "" : "\n ORDER BY " + orderBy.map(Sort::sql).collect(Collectors.joining(", ")));
    }

    public DBStatementBinder bind() {
        return ps -> {
            int pos = 1;
            for (Condition condition : where) {
                pos = condition.bind(ps, pos);
            }
        };
    }
}
