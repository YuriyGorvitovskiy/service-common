package org.service.action.schema.definition;

import org.service.action.Action;
import org.service.action.IAction;
import org.service.action.Result;
import org.service.action.schema.Schema;
import org.service.action.schema.Schema.IndexColumns;
import org.service.action.schema.Schema.Indexes;
import org.service.action.schema.Schema.Table;
import org.service.action.schema.Service;
import org.service.immutable.data.Patch;
import org.service.immutable.data.Patch.Operation;
import org.service.immutable.data.Row;

import io.vavr.Tuple2;

@Action(service = Service.DEFINITION, name = Service.Drop.INDEX)
public class DropIndex implements IAction<DropIndex.Params, DropIndex.Context> {

    public static class Params {
        public final Long id;

        public Params(Long id) {
            this.id = id;
        }
    }

    public static class Context {
    }

    @Override
    public Result apply(Params params, Context ctx) {
        return Result.of(
                new Patch(Operation.delete,
                        Row.of(Schema.NAME,
                                Table.INDEXES,
                                new Tuple2<>(Indexes.ID, params.id))),
                new Patch(Operation.delete,
                        Row.of(Schema.NAME,
                                Table.INDEX_COLUMNS,
                                new Tuple2<>(IndexColumns.INDEX, params.id))));
    }
}
