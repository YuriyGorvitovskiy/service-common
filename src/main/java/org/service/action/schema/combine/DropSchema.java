package org.service.action.schema.combine;

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

@Action(service = "schema_manager", name = "drop_schema")
public class DropSchema implements IAction<DropSchema.Params, DropSchema.Context> {

    public static class Params {
        public final String name;

        Params(String name) {
            this.name = name;
        }
    }

    public static class Table extends DropTable.Table {

        public final String name;

        Table(Long id, String name, List<DropTable.Column> columns, List<DropTable.Index> indexes) {
            super(id, columns, indexes);
            this.name = name;
        }
    }

    public static class Schema {

        public final Long        id;

        @From(schema = "model", table = "tables")
        @Where({ @Equal(column = "schema", context = "id") })
        public final List<Table> tables;

        Schema(Long id, List<Table> tables) {
            this.id = id;
            this.tables = tables;
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
        // Schema could contains a lot of tables,
        // In this case DROP SCHEMA could exceed transaction limit.
        // So let us drop all tables individually.
        DropTable   dropTable = new DropTable();
        List<Patch> patches   = ctx.schema.tables.flatMap(t -> dropTable.apply(
                new DropTable.Params(params.name, t.name),
                new DropTable.Context(ctx.dbc,
                        new DropTable.Schema(ctx.schema.id, t))).patches);

        String      ddl       = "DROP SCHEMA " + params.name + " CASCADE";
        try (PreparedStatement ps = ctx.dbc.prepareStatement(ddl)) {
            ps.execute();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return Result.ofPatches(patches
            .append(new Patch(Operation.delete, Row.of("model", "schemas", new Tuple2<>("id", ctx.schema.id)))));
    }
}
