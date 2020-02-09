package org.service.action.schema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Supplier;

import org.service.action.Action;
import org.service.action.Counter;
import org.service.action.IAction;
import org.service.action.Result;
import org.service.immutable.data.Patch;
import org.service.immutable.data.Patch.Operation;
import org.service.immutable.data.Row;

import io.vavr.Tuple2;
import io.vavr.collection.List;

@Action(service = "schema_manager", name = "create_schema")
public class CreateSchema implements IAction<CreateSchema.Params, CreateSchema.Context> {

    public static class Params {
        public final String                   name;
        public final List<CreateTable.Params> tables;

        Params(String name, List<CreateTable.Params> tables) {
            this.name = name;
            this.tables = tables;
        }
    }

    public static class Context {

        @Counter("schema_id")
        public final Supplier<Long> schema_id_counter;
        public final Supplier<Long> table_id_counter;
        public final Supplier<Long> column_id_counter;
        public final Supplier<Long> index_id_counter;

        public final Connection     dbc;

        Context(Supplier<Long> schema_id_counter,
                Supplier<Long> table_id_counter,
                Supplier<Long> column_id_counter,
                Supplier<Long> index_id_counter,
                Connection dbc) {
            this.schema_id_counter = schema_id_counter;
            this.table_id_counter = table_id_counter;
            this.column_id_counter = column_id_counter;
            this.index_id_counter = index_id_counter;
            this.dbc = dbc;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        String ddl = "CREATE SCHEMA " + params.name;
        try (PreparedStatement ps = ctx.dbc.prepareStatement(ddl)) {
            ps.execute();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        Long        id          = ctx.schema_id_counter.get();
        List<Patch> patches     = List.of(new Patch(Operation.insert,
                Row.of("model",
                        "schemas",
                        new Tuple2<>("id", id),
                        new Tuple2<>("name", params.name))));

        CreateTable createTable = new CreateTable();
        for (CreateTable.Params table : params.tables) {
            patches.appendAll(createTable.apply(table,
                    new CreateTable.Context(
                            ctx.table_id_counter,
                            ctx.column_id_counter,
                            ctx.index_id_counter,
                            ctx.dbc,
                            new CreateTable.Schema(id))).patches);
        }

        return Result.ofPatches(patches);
    }
}
