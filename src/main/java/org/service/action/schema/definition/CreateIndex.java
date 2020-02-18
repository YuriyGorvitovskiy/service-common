package org.service.action.schema.definition;

import org.service.action.Action;
import org.service.action.IAction;
import org.service.action.Result;
import org.service.action.Sequence;
import org.service.action.schema.Schema;
import org.service.action.schema.Schema.IndexColumns;
import org.service.action.schema.Schema.Indexes;
import org.service.action.schema.Schema.Table;
import org.service.action.schema.Service;
import org.service.immutable.data.Patch;
import org.service.immutable.data.Patch.Operation;
import org.service.immutable.data.Row;

import io.vavr.Tuple2;
import io.vavr.collection.List;

@Action(service = Service.DEFINITION, name = Service.Create.INDEX)
public class CreateIndex implements IAction<CreateIndex.Params, CreateIndex.Context> {

    public static class Params {
        public final Long       table_id;
        public final String     name;
        public final Boolean    primary;
        public final List<Long> column_ids;

        public Params(Long table_id, String name, Boolean primary, List<Long> column_ids) {
            this.table_id = table_id;
            this.name = name;
            this.primary = primary;
            this.column_ids = column_ids;
        }
    }

    public static class Context {

        @Sequence(Schema.Sequence.INDEX_ID)
        public final Long new_index_id;

        Context(Long new_index_id) {
            this.new_index_id = new_index_id;
        }

    }

    @Override
    public Result apply(Params params, Context ctx) {
        return Result.ofPatches(columnPatches(params, ctx).prepend(indexPatch(params, ctx)));
    }

    Patch indexPatch(Params params, Context ctx) {
        return new Patch(Operation.insert,
                Row.of(Schema.NAME,
                        Table.INDEXES,
                        new Tuple2<>(Indexes.ID, ctx.new_index_id),
                        new Tuple2<>(Indexes.TABLE, params.table_id),
                        new Tuple2<>(Indexes.NAME, params.name),
                        new Tuple2<>(Indexes.PRIMARY, params.primary)));
    }

    List<Patch> columnPatches(Params params, Context ctx) {
        return params.column_ids
            .map(c -> Row.of(
                    Schema.NAME,
                    Table.INDEX_COLUMNS,
                    new Tuple2<>(IndexColumns.INDEX, ctx.new_index_id),
                    new Tuple2<>(IndexColumns.COLUMN, c)))
            .map(r -> new Patch(Operation.insert, r));
    }
}
