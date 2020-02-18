package org.service.action.schema.definition;

import org.service.action.Action;
import org.service.action.Equal;
import org.service.action.ForEach;
import org.service.action.From;
import org.service.action.IAction;
import org.service.action.Key;
import org.service.action.Result;
import org.service.action.Where;
import org.service.immutable.data.Patch;
import org.service.immutable.data.Patch.Operation;
import org.service.immutable.data.Row;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;

@Action(service = "schema_manager", name = "drop_schema")
public class DropSchema implements IAction<DropSchema.Params, DropSchema.Context> {

    public static class Params {
        public final Long id;

        Params(Long id) {
            this.id = id;
        }
    }

    public static class Context {
        @From(schema = "model", table = "tables")
        @Where({ @Equal(column = "schema_id", param = "id") })
        public final List<DropTable.Params> tables;

        @ForEach(context = "tables")
        @Key("id")
        public final Map<Long, DropTable.Context> tableCtx;

        @From(schema = "model", table = "sequences")
        @Where({ @Equal(column = "schema_id", param = "id") })
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
                Row.of("model",
                        "schemas",
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
