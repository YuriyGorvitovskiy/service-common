package org.service.actions.patch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.stream.Collectors;

import org.service.action.Action;
import org.service.action.Equal;
import org.service.action.From;
import org.service.action.IAction;
import org.service.action.OrderBy;
import org.service.action.Result;
import org.service.action.Where;
import org.service.immutable.data.Row;
import org.service.immutable.schema.DataType;

import io.vavr.collection.List;

@Action(service = "persistence", name = "update")
public class UpdateRow implements IAction<Row, UpdateRow.Context> {

    public static class Column {

        public final String   name;

        public final DataType type;

        Column(String name, DataType type) {
            this.name = name;
            this.type = type;
        }
    }

    public static class ColumnId {
        public final Long   id;

        @From(schema = "model", table = "columns")
        @Where({
                @Equal(column = "id", context = "id"),
        })
        public final Column column;

        ColumnId(Long id, Column column) {
            this.id = id;
            this.column = column;
        }
    }

    public static class Index {

        public final Long           id;

        @From(schema = "model", table = "index_columns")
        @Where({
                @Equal(column = "id", context = "id"),
        })
        @OrderBy(column = "position")
        public final List<ColumnId> columns;

        Index(Long id, List<ColumnId> columns) {
            this.id = id;
            this.columns = columns;
        }
    }

    public static class Table {

        public final Long         id;

        public final String       name;

        @From(schema = "model", table = "columns")
        @Where({
                @Equal(column = "table", context = "id"),
        })
        @OrderBy(column = "position")
        public final List<Column> columns;

        @From(schema = "model", table = "indexes")
        @Where({
                @Equal(column = "table", context = "id"),
                @Equal(column = "primary", value = "true"),
        })
        public final Index        primary;

        Table(Long id, String name, List<Column> columns, Index primary) {
            this.id = id;
            this.name = name;
            this.columns = columns;
            this.primary = primary;
        }
    }

    public static class Schema {

        public final Long   id;

        public final String name;

        @From(schema = "model", table = "tables")
        @Where({
                @Equal(column = "schema", context = "id"),
                @Equal(column = "name", param = "table")
        })
        public final Table  table;

        Schema(Long id, String name, Table table) {
            this.id = id;
            this.name = name;
            this.table = table;
        }
    }

    public static class Context {

        public final Connection dbc;

        @From(schema = "model", table = "schemas")
        @Where({
                @Equal(column = "name", param = "schema")
        })
        public final Schema     schema;

        Context(Connection dbc, Schema schema) {
            this.dbc = dbc;
            this.schema = schema;
        }

    }

    @Override
    public Result apply(Row params, Context ctx) throws Exception {
        List<Column> primary = ctx.schema.table.primary.columns
            .map(c -> c.column)
            .filter(c -> params.values.containsKey(c.name));

        List<Column> columns = ctx.schema.table.columns
            .filter(c -> params.values.containsKey(c.name))
            .filter(c -> !primary.exists(p -> Objects.equals(c.name, p.name)));

        String       dml     = "UPDATE " + params.schema + "." + params.table + " SET " +
                columns.map(c -> c.name + " = ?").collect(Collectors.joining(", ")) +
                " WHERE " + primary.map(c -> c.name + " = ?").collect(Collectors.joining(" AND "));

        try (PreparedStatement ps = ctx.dbc.prepareStatement(dml)) {
            int i = 1;
            for (Column column : columns) {
                column.type.set(ps, i++, params, column.name);
            }
            for (Column column : primary) {
                column.type.set(ps, i++, params, column.name);
            }
            ps.execute();
        }
        return Result.empty;
    }

}
