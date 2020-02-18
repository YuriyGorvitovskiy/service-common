package org.service.action.schema.definition;

import java.util.function.Supplier;

import org.service.action.Action;
import org.service.action.IAction;
import org.service.action.Result;
import org.service.action.Sequence;
import org.service.immutable.data.Patch;
import org.service.immutable.data.Patch.Operation;
import org.service.immutable.data.Row;

import io.vavr.Tuple2;

@Action(service = "schema_manager", name = "create_column")
public class CreateSequence implements IAction<CreateSequence.Params, CreateSequence.Context> {

    public static class Params {
        public final Long   schema_id;
        public final String name;
        public final Long   start;

        Params(Long schema_id, String name, Long start) {
            this.schema_id = schema_id;
            this.name = name;
            this.start = start;
        }
    }

    public static class Context {

        @Sequence("sequence_id")
        public final Supplier<Long> new_sequence_id;

        Context(Supplier<Long> new_sequence_id) {
            this.new_sequence_id = new_sequence_id;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        return Result.of(new Patch(Operation.insert,
                Row.of("model",
                        "sequences",
                        new Tuple2<>("id", ctx.new_sequence_id),
                        new Tuple2<>("schema", params.schema_id),
                        new Tuple2<>("name", params.name))));
    }
}
