package org.service.action.schema.combine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Supplier;

import org.service.action.Action;
import org.service.action.Equal;
import org.service.action.From;
import org.service.action.IAction;
import org.service.action.Result;
import org.service.action.Sequence;
import org.service.action.Where;
import org.service.immutable.data.Patch;
import org.service.immutable.data.Patch.Operation;
import org.service.immutable.data.Row;
import org.service.immutable.schema.DataType;

import io.vavr.Tuple2;

@Action(service = "schema_manager", name = "create_column")
public class CreateColumn implements IAction<CreateColumn.Params, CreateColumn.Context> {

    public static class Params {
        public final String   schema;
        public final String   table;
        public final String   name;
        public final DataType type;

        Params(String schema, String table, String name, DataType type) {
            this.schema = schema;
            this.table = table;
            this.name = name;
            this.type = type;
        }
    }

    public static class Table {

        public final Long id;

        Table(Long id) {
            this.id = id;
        }
    }

    public static class Schema {

        public final Long  id;

        @From(schema = "model", table = "tables")
        @Where({
                @Equal(column = "schema", context = "id"),
                @Equal(column = "name", param = "table")
        })
        public final Table table;

        Schema(Long id, Table table) {
            this.id = id;
            this.table = table;
        }
    }

    public static class Context {

        @Sequence("column_id")
        public final Supplier<Long> seq_column_id;

        public final Connection     dbc;

        @From(schema = "model", table = "schemas")
        @Where({ @Equal(column = "name", param = "schema") })
        public final Schema         schema;

        Context(Supplier<Long> seq_column_id, Connection dbc, Schema schema) {
            this.seq_column_id = seq_column_id;
            this.dbc = dbc;
            this.schema = schema;
        }

    }

    @Override
    public Result apply(Params params, Context ctx) {
        String ddl = "ALTER TABLE " + params.schema + "." + params.table + " ADD COLUMN " + columnDDL(params);
        try (PreparedStatement ps = ctx.dbc.prepareStatement(ddl)) {
            ps.execute();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return Result.of(patch(params, ctx));

    }

    public String columnDDL(Params params) {
        return params.name + " " + params.type.postgres();
    }

    public Patch patch(Params params, Context ctx) {
        return new Patch(Operation.insert,
                Row.of("model",
                        "columns",
                        new Tuple2<>("id", ctx.seq_column_id.get()),
                        new Tuple2<>("table", ctx.schema.table.id),
                        new Tuple2<>("name", params.name),
                        new Tuple2<>("type", params.type.name())));
    }
}
