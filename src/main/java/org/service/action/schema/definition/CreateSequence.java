package org.service.action.schema.definition;

import java.util.function.Supplier;

import org.service.action.Action;
import org.service.action.IAction;
import org.service.action.Result;
import org.service.action.Sequence;
import org.service.action.schema.Schema;
import org.service.action.schema.Schema.Sequences;
import org.service.action.schema.Service;
import org.service.immutable.data.Patch;
import org.service.immutable.data.Patch.Operation;
import org.service.immutable.data.Row;

import io.vavr.Tuple2;

@Action(service = Service.DEFINITION, name = Service.Create.SEQUENCE)
public class CreateSequence implements IAction<CreateSequence.Params, CreateSequence.Context> {

    public static class Params {
        public final Long   schema_id;
        public final String name;

        public Params(Long schema_id, String name) {
            this.schema_id = schema_id;
            this.name = name;
        }
    }

    public static class Context {

        @Sequence(Schema.Sequence.SEQUENCE_ID)
        public final Supplier<Long> new_sequence_id;

        Context(Supplier<Long> new_sequence_id) {
            this.new_sequence_id = new_sequence_id;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        return Result.of(new Patch(Operation.insert,
                Row.of(Schema.NAME,
                        Schema.Table.SEQUENCES,
                        new Tuple2<>(Sequences.ID, ctx.new_sequence_id),
                        new Tuple2<>(Sequences.SCHEMA, params.schema_id),
                        new Tuple2<>(Sequences.NAME, params.name))));
    }
}
