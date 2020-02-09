package org.service.actions.patch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
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

@Action(service = "persistence", name = "insert")
public class InsertRow implements IAction<Row, InsertRow.Context> {

    public static class Column {

        public final String   name;

        public final DataType type;

        Column(String name, DataType type) {
            this.name = name;
            this.type = type;
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

        Table(Long id, String name, List<Column> columns) {
            this.id = id;
            this.name = name;
            this.columns = columns;
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
    public Result apply(Row params, Context ctx) {
        List<Column> columns = ctx.schema.table.columns.filter(c -> params.values.containsKey(c.name));

        String       dml     = "INSERT INTO " + params.schema + "." + params.table + "(" +
                columns.map(c -> c.name).collect(Collectors.joining(", ")) +
                ") VALUES (" + StringUtils.repeat("?", ", ", columns.size()) + ")";
        try (PreparedStatement ps = ctx.dbc.prepareStatement(dml)) {
            int i = 1;
            for (Column column : columns) {
                column.type.set(ps, i++, params, column.name);
            }
            ps.execute();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return Result.empty;
    }

}
