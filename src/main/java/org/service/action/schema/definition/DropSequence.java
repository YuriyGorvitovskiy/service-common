package org.service.action.schema.definition;

import org.service.action.Action;
import org.service.action.IAction;
import org.service.action.Result;
import org.service.immutable.data.Patch;
import org.service.immutable.data.Patch.Operation;
import org.service.immutable.data.Row;

import io.vavr.Tuple2;

@Action(service = "schema_manager", name = "drop_table")
public class DropSequence implements IAction<DropSequence.Params, DropSequence.Context> {

    public static class Params {
        public final Long id;

        Params(Long id) {
            this.id = id;
        }
    }

    public static class Context {
    }

    @Override
    public Result apply(Params params, Context ctx) {
        return Result.of(new Patch(Operation.delete, Row.of("model", "sequences", new Tuple2<>("id", params.id))));
    }
}
