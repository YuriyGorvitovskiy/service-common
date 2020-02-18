package org.service.action.schema.definition;

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
        public final Long id;

        public Params(Long id) {
            this.id = id;
        }
    }

    public static class Context {

        @From(schema = "model", table = "columns")
        @Where({ @Equal(column = "table_id", param = "id") })
        public final List<DropColumn.Params> columns;

        @From(schema = "model", table = "indexes")
        @Where({ @Equal(column = "table_id", param = "id") })
        public final List<DropIndex.Params> indexes;

        Context(List<DropIndex.Params> indexes, List<DropColumn.Params> columns) {
            this.indexes = indexes;
            this.columns = columns;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        List<Patch> tablePatches = List.of(new Patch(Operation.delete,
                Row.of("model",
                        "tables",
                        new Tuple2<>("id", params.id))));

        DropColumn  dropColumn    = new DropColumn();
        List<Patch> columnPatches = ctx.columns.flatMap(p -> dropColumn.columnPatches(p));

        DropIndex   dropIndex    = new DropIndex();
        List<Patch> indexPatches = ctx.indexes.flatMap(p -> dropIndex.apply(p, new DropIndex.Context()).patches);

        return Result.ofPatches(indexPatches, columnPatches, tablePatches);
    }
}
