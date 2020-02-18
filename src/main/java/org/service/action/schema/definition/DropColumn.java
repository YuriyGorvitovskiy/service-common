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

@Action(service = "schema_manager", name = "drop_column")
public class DropColumn implements IAction<DropColumn.Params, DropColumn.Context> {

    public static class Params {
        public final Long id;

        Params(Long id) {
            this.id = id;
        }
    }

    public static class Context {

        @From(schema = "model", table = "index_columns")
        @Where({ @Equal(column = "column", param = "id") })
        public final List<DropIndex.Params> indexes;

        Context(List<DropIndex.Params> indexes) {
            this.indexes = indexes;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        List<Patch> columnPatches = columnPatches(params);

        final DropIndex dropIndex    = new DropIndex();
        List<Patch>     indexPatches = ctx.indexes
            .flatMap(p -> dropIndex.apply(p, new DropIndex.Context()).patches);

        return Result.ofPatches(indexPatches, columnPatches);
    }

    List<Patch> columnPatches(Params params) {
        return List.of(new Patch(Operation.delete, Row.of("model", "columns", new Tuple2<>("id", params.id))));
    }

}
