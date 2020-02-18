package org.service.action.schema.definition;

import org.service.action.Action;
import org.service.action.Equal;
import org.service.action.From;
import org.service.action.IAction;
import org.service.action.Result;
import org.service.action.Where;
import org.service.action.schema.Schema;
import org.service.action.schema.Schema.Columns;
import org.service.action.schema.Schema.IndexColumns;
import org.service.action.schema.Schema.Table;
import org.service.action.schema.Service;
import org.service.immutable.data.Patch;
import org.service.immutable.data.Patch.Operation;
import org.service.immutable.data.Row;

import io.vavr.Tuple2;
import io.vavr.collection.List;

@Action(service = Service.DEFINITION, name = Service.Drop.COLUMN)
public class DropColumn implements IAction<DropColumn.Params, DropColumn.Context> {

    public static class Params {
        public final Long id;

        Params(Long id) {
            this.id = id;
        }
    }

    public static class Context {

        @From(schema = Schema.NAME, table = Table.INDEX_COLUMNS)
        @Where({ @Equal(column = IndexColumns.COLUMN, param = "id") })
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
        return List.of(new Patch(Operation.delete,
                Row.of(Schema.NAME,
                        Table.COLUMNS,
                        new Tuple2<>(Columns.ID, params.id))));
    }

}
