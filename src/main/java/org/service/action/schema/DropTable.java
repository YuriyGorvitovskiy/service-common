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
import org.service.action.schema.Schema.Tables;

@Action(service = Service.SCHEMA, name = Service.Create.COLUMN)
public class DropTable implements IAction<DropTable.Params, DropTable.Context> {

    public static class Params {
        public final String schema;
        public final String table;

        public Params(String schema,
                      String table) {
            this.schema = schema;
            this.table = table;
        }
    }

    public static class Context {
        @Select(alias = "t", value = "id")
        @Join({
                @From(schema = Schema.NAME, table = Table.TABLES, alias = "t"),
                @From(schema = Schema.NAME, table = Table.SCHEMAS, alias = "s", on = @Equal(left = @Operand(alias = "s", column = Schemas.ID), right = @Operand(alias = "t", column = Sequences.SCHEMA)))
        })
        @Where({
                @Equal(left = @Operand(alias = "t", column = Tables.NAME), right = @Operand(param = "table")),
                @Equal(left = @Operand(alias = "s", column = Schemas.NAME), right = @Operand(param = "schema"))
        })
        public final Long table_id;

        public Context(Long table_id) {
            this.table_id = table_id;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        return Result.of(
                new Event<>(
                        Service.POSTGRES,
                        Service.Drop.TABLE,
                        new org.service.action.schema.postgres.DropTable.Params(
                                params.schema,
                                params.table)),
                new Event<>(
                        Service.DEFINITION,
                        Service.Drop.TABLE,
                        new org.service.action.schema.definition.DropTable.Params(ctx.table_id)));
    }

}
