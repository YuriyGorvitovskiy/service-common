package org.service.action.schema;

import org.service.action.Action;
import org.service.action.Equal;
import org.service.action.Event;
import org.service.action.From;
import org.service.action.IAction;
import org.service.action.Join;
import org.service.action.Operand;
import org.service.action.Result;
import org.service.action.Select;
import org.service.action.Where;
import org.service.action.schema.Schema.Schemas;
import org.service.action.schema.Schema.Sequences;
import org.service.action.schema.Schema.Table;

@Action(service = Service.SCHEMA, name = Service.Create.COLUMN)
public class DropSequence implements IAction<DropSequence.Params, DropSequence.Context> {

    public static class Params {
        public final String schema;
        public final String sequence;

        public Params(String schema,
                      String sequence) {
            this.schema = schema;
            this.sequence = sequence;
        }
    }

    public static class Context {
        @Select(alias = "q", value = "id")
        @Join({
                @From(schema = Schema.NAME, table = Table.SEQUENCES, alias = "q"),
                @From(schema = Schema.NAME, table = Table.SCHEMAS, alias = "s", on = @Equal(left = @Operand(alias = "s", column = Schemas.ID), right = @Operand(alias = "q", column = Sequences.SCHEMA)))
        })
        @Where({
                @Equal(left = @Operand(alias = "q", column = Sequences.NAME), right = @Operand(param = "sequence")),
                @Equal(left = @Operand(alias = "s", column = Schemas.NAME), right = @Operand(param = "schema"))
        })
        public final Long sequence_id;

        public Context(Long sequence_id) {
            this.sequence_id = sequence_id;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        return Result.of(
                new Event<>(
                        Service.POSTGRES,
                        Service.Drop.SEQUENCE,
                        new org.service.action.schema.postgres.DropSequence.Params(
                                params.schema,
                                params.sequence)),
                new Event<>(
                        Service.DEFINITION,
                        Service.Drop.SEQUENCE,
                        new org.service.action.schema.definition.DropSequence.Params(ctx.sequence_id)));
    }

}
