package org.service.action.schema.definition;

import org.service.action.Action;
import org.service.action.ForEach;
import org.service.action.IAction;
import org.service.action.Key;
import org.service.action.Result;
import org.service.action.Sequence;
import org.service.action.schema.Schema;
import org.service.action.schema.Schema.Schemas;
import org.service.action.schema.Schema.Table;
import org.service.action.schema.Service;
import org.service.immutable.data.Patch;
import org.service.immutable.data.Patch.Operation;
import org.service.immutable.data.Row;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;

@Action(service = Service.DEFINITION, name = Service.Create.SCHEMA)
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

        @Sequence(Schema.Sequence.SCHEMA_ID)
        public final Long new_schema_id;

        @ForEach(param = "tables")
        @Key("name")
        public final Map<String, CreateTable.Context> tables;

        @ForEach(param = "sequences")
        @Key("name")
        public final Map<String, CreateSequence.Context> sequences;

        Context(Long seq_schema_id,
                Map<String, CreateTable.Context> tables,
                Map<String, CreateSequence.Context> sequences) {
            this.new_schema_id = seq_schema_id;
            this.tables = tables;
            this.sequences = sequences;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        List<Patch> schemaPatches = List.of(new Patch(Operation.insert,
                Row.of(Schema.NAME,
                        Table.SCHEMAS,
                        new Tuple2<>(Schemas.ID, ctx.new_schema_id),
                        new Tuple2<>(Schemas.NAME, params.name))));

        final CreateTable createTable  = new CreateTable();
        List<Patch>       tablePatches = params.tables
            .flatMap(p -> createTable.apply(p, ctx.tables.get(p.name).get()).patches);

        final CreateSequence createSequence  = new CreateSequence();
        List<Patch>          sequencePatches = params.sequences
            .flatMap(p -> createSequence.apply(p, ctx.sequences.get(p.name).get()).patches);

        return Result.ofPatches(schemaPatches, tablePatches, sequencePatches);
    }
}
