package org.service.action.schema;

import org.service.action.Action;
import org.service.action.Equal;
import org.service.action.Event;
import org.service.action.From;
import org.service.action.IAction;
import org.service.action.Operand;
import org.service.action.Result;
import org.service.action.Select;
import org.service.action.Where;
import org.service.action.schema.Schema.Schemas;
import org.service.action.schema.Schema.Table;

@Action(service = Service.SCHEMA, name = Service.Create.SEQUENCE)
public class CreateSequence implements IAction<CreateSequence.Params, CreateSequence.Context> {

    public static class Params extends Sequence {
        public final String schema;

        public Params(String schema,
                      String name,
                      Long start) {
            super(name, start);
            this.schema = schema;
        }
    }

    public static class Sequence {
        public final String name;
        public final Long   start;

        public Sequence(String name,
                        Long start) {
            this.name = name;
            this.start = start;
        }
    }

    public static class Context {
        @Select(Schemas.ID)
        @From(schema = Schema.NAME, table = Table.SCHEMAS)
        @Where({ @Equal(left = @Operand(column = Schemas.NAME), right = @Operand(param = "schema")) })
        public final Long schema_id;

        public Context(Long schema_id) {
            this.schema_id = schema_id;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        return Result.of(
                new Event<>(
                        Service.POSTGRES,
                        Service.Create.SEQUENCE,
                        new org.service.action.schema.postgres.CreateSequence.Params(
                                params.schema,
                                params.name,
                                params.start)),
                new Event<>(
                        Service.DEFINITION,
                        Service.Create.SEQUENCE,
                        new org.service.action.schema.definition.CreateSequence.Params(
                                ctx.schema_id,
                                params.name)));
    }

}
