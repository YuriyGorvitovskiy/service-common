package org.service.action.schema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.service.action.Action;
import org.service.action.Equal;
import org.service.action.From;
import org.service.action.IAction;
import org.service.action.Result;
import org.service.action.Where;
import org.service.immutable.data.Patch;
import org.service.immutable.data.Patch.Operation;
import org.service.immutable.data.Row;

import io.vavr.Tuple2;
import io.vavr.collection.List;

@Action(service = "schema_manager", name = "drop_table")
public class DropTable implements IAction<DropTable.Params, DropTable.Context> {

    public static class Params {
        public final String schema;
        public final String name;

        Params(String schema, String name) {
            this.schema = schema;
            this.name = name;
        }
    }

    public static class Column {

        public final Long id;

        Column(Long id) {
            this.id = id;
        }
    }

    public static class Index {

        public final Long id;

        Index(Long id) {
            this.id = id;
        }
    }

    public static class Table {

        public final Long         id;

        @From(schema = "model", table = "columns")
        @Where({ @Equal(column = "table", context = "id") })
        public final List<Column> columns;

        @From(schema = "model", table = "indexes")
        @Where({ @Equal(column = "table", context = "id") })
        public final List<Index>  indexes;

        Table(Long id, List<Column> columns, List<Index> indexes) {
            this.id = id;
            this.columns = columns;
            this.indexes = indexes;
        }
    }

    public static class Schema {

        public final Long  id;

        @From(schema = "model", table = "tables")
        @Where({
                @Equal(column = "schema", context = "id"),
                @Equal(column = "name", param = "name")
        })
        public final Table table;

        Schema(Long id, Table table) {
            this.id = id;
            this.table = table;
        }
    }

    public static class Context {

        public final Connection dbc;

        @From(schema = "model", table = "schemas")
        @Where({ @Equal(column = "name", param = "schema") })
        public final Schema     schema;

        Context(Connection dbc, Schema schema) {
            this.dbc = dbc;
            this.schema = schema;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        String ddl = "DROP TABLE " + params.schema + "." + params.name + " CASCADE";
        try (PreparedStatement ps = ctx.dbc.prepareStatement(ddl)) {
            ps.execute();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        List<Index>  indexes = ctx.schema.table.indexes;
        List<Column> columns = ctx.schema.table.columns;
        Long         id      = ctx.schema.table.id;
        return Result.ofPatches(
                indexes.map(i -> new Patch(Operation.delete, Row.of("model", "index_columns", new Tuple2<>("id", i)))),
                indexes.map(i -> new Patch(Operation.delete, Row.of("model", "indexes", new Tuple2<>("id", i)))),
                columns.map(i -> new Patch(Operation.delete, Row.of("model", "columns", new Tuple2<>("id", i)))),
                List.of(new Patch(Operation.delete, Row.of("model", "tables", new Tuple2<>("id", id)))));
    }
}
