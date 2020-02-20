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

@Action(service = Service.SCHEMA, name = Service.Create.COLUMN)
public class DropSchema implements IAction<DropSchema.Params, DropSchema.Context> {

    public static class Params {
        public final String schema;

        public Params(String schema) {
            this.schema = schema;
        }
    }

    public static class Context {
        @Select(alias = "t", value = "id")
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
                        Service.Drop.SCHEMA,
                        new org.service.action.schema.postgres.DropSchema.Params(
                                params.schema)),
                new Event<>(
                        Service.DEFINITION,
                        Service.Drop.SCHEMA,
                        new org.service.action.schema.definition.DropSchema.Params(ctx.schema_id)));
    }

}
