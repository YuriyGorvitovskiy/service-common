package org.service.action.schema.original;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Supplier;

import org.service.action.Action;
import org.service.action.IAction;
import org.service.action.Result;
import org.service.action.Sequence;
import org.service.immutable.data.Patch;
import org.service.immutable.data.Patch.Operation;
import org.service.immutable.data.Row;

import io.vavr.Tuple2;
import io.vavr.collection.List;

@Action(service = "schema_manager", name = "create_schema")
public class CreateSchema implements IAction<CreateSchema.Params, CreateSchema.Context> {

    public static class Params {
        public final String                      name;
        public final List<CreateTable.Params>    tables;
        public final List<CreateSequence.Params> sequences;

        Params(String name, List<CreateTable.Params> tables, List<CreateSequence.Params> sequences) {
            this.name = name;
            this.tables = tables;
            this.sequences = sequences;
        }
    }

    public static class Context {

        @Sequence("schema_id")
        public final Supplier<Long> seq_schema_id;

        @Sequence("table_id")
        public final Supplier<Long> seq_table_id;

        @Sequence("column_id")
        public final Supplier<Long> seq_column_id;

        @Sequence("index_id")
        public final Supplier<Long> seq_index_id;

        @Sequence("sequence_id")
        public final Supplier<Long> seq_sequence_id;

        public final Connection     dbc;

        Context(Supplier<Long> seq_schema_id,
                Supplier<Long> seq_table_id,
                Supplier<Long> seq_column_id,
                Supplier<Long> seq_index_id,
                Supplier<Long> seq_sequence_id,
                Connection dbc) {
            this.seq_schema_id = seq_schema_id;
            this.seq_table_id = seq_table_id;
            this.seq_column_id = seq_column_id;
            this.seq_index_id = seq_index_id;
            this.seq_sequence_id = seq_sequence_id;
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

        Long        id          = ctx.seq_schema_id.get();
        List<Patch> patches     = List.of(new Patch(Operation.insert,
                Row.of("model",
                        "schemas",
                        new Tuple2<>("id", id),
                        new Tuple2<>("name", params.name))));

        CreateTable createTable = new CreateTable();
        for (CreateTable.Params table : params.tables) {
            patches.appendAll(createTable.apply(table,
                    new CreateTable.Context(
                            ctx.seq_table_id,
                            ctx.seq_column_id,
                            ctx.seq_index_id,
                            ctx.dbc,
                            new CreateTable.Schema(id))).patches);
        }

        CreateSequence createSequence = new CreateSequence();
        for (CreateSequence.Params sequence : params.sequences) {
            patches.appendAll(createSequence.apply(sequence,
                    new CreateSequence.Context(
                            ctx.seq_sequence_id,
                            ctx.dbc,
                            new CreateSequence.Schema(id))).patches);
        }

        return Result.ofPatches(patches);
    }
}
