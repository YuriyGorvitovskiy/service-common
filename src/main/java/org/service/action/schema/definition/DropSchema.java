package org.service.action.schema.definition;

import org.service.action.Action;
import org.service.action.Equal;
import org.service.action.ForEach;
import org.service.action.From;
import org.service.action.IAction;
import org.service.action.Key;
import org.service.action.Result;
import org.service.action.Where;
import org.service.action.schema.Schema;
import org.service.action.schema.Schema.Sequences;
import org.service.action.schema.Schema.Table;
import org.service.action.schema.Schema.Tables;
import org.service.action.schema.Service;
import org.service.immutable.data.Patch;
import org.service.immutable.data.Patch.Operation;
import org.service.immutable.data.Row;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;

@Action(service = Service.DEFINITION, name = Service.Drop.SCHEMA)
public class DropSchema implements IAction<DropSchema.Params, DropSchema.Context> {

    public static class Params {
        public final Long id;

        Params(Long id) {
            this.id = id;
        }
    }

    public static class Context {
        @From(schema = Schema.NAME, table = Table.TABLES)
        @Where({ @Equal(column = Tables.SCHEMA, param = "id") })
        public final List<DropTable.Params> tables;

        @ForEach(context = "tables")
        @Key("id")
        public final Map<Long, DropTable.Context> tableCtx;

        @From(schema = Schema.NAME, table = Table.SEQUENCES)
        @Where({ @Equal(column = Sequences.SCHEMA, param = "id") })
        public final List<DropSequence.Params> sequences;

        Context(List<DropTable.Params> tables, Map<Long, DropTable.Context> tableCtx, List<DropSequence.Params> sequences) {
            this.tables = tables;
            this.tableCtx = tableCtx;
            this.sequences = sequences;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        List<Patch> schemaPatches = List.of(new Patch(Operation.delete,
                Row.of(Schema.NAME,
                        Table.SCHEMAS,
                        new Tuple2<>("id", params.id))));

        DropTable   dropTable    = new DropTable();
        List<Patch> tablePatches = ctx.tables
            .flatMap(p -> dropTable.apply(p, ctx.tableCtx.get(p.id).get()).patches);

        DropSequence dropSequence    = new DropSequence();
        List<Patch>  sequencePatches = ctx.sequences
            .flatMap(p -> dropSequence.apply(p, new DropSequence.Context()).patches);

        return Result.ofPatches(tablePatches, sequencePatches, schemaPatches);
    }
}
