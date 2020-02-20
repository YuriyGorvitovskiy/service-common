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
import org.service.action.schema.Schema.Indexes;
import org.service.action.schema.Schema.Schemas;
import org.service.action.schema.Schema.Table;
import org.service.action.schema.Schema.Tables;

@Action(service = Service.SCHEMA, name = Service.Create.COLUMN)
public class DropIndex implements IAction<DropIndex.Params, DropIndex.Context> {

    public static class Params {
        public final String schema;
        public final String table;
        public final String index;

        public Params(String schema,
                      String table,
                      String index) {
            this.schema = schema;
            this.table = table;
            this.index = index;
        }
    }

    public static class Context {
        @Select(alias = "i", value = "id")
        @Join({
                @From(schema = Schema.NAME, table = Table.INDEXES, alias = "i"),
                @From(schema = Schema.NAME, table = Table.TABLES, alias = "t", on = @Equal(left = @Operand(alias = "t", column = Tables.ID), right = @Operand(alias = "i", column = Indexes.TABLE))),
                @From(schema = Schema.NAME, table = Table.SCHEMAS, alias = "s", on = @Equal(left = @Operand(alias = "s", column = Schemas.ID), right = @Operand(alias = "t", column = Tables.SCHEMA)))
        })
        @Where({
                @Equal(left = @Operand(alias = "i", column = Indexes.NAME), right = @Operand(param = "index")),
                @Equal(left = @Operand(alias = "s", column = Schemas.NAME), right = @Operand(param = "schema"))
        })
        public final Long index_id;

        public Context(Long index_id) {
            this.index_id = index_id;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        return Result.of(
                new Event<>(
                        Service.POSTGRES,
                        Service.Drop.INDEX,
                        new org.service.action.schema.postgres.DropIndex.Params(
                                params.schema,
                                params.index)),
                new Event<>(
                        Service.DEFINITION,
                        Service.Drop.INDEX,
                        new org.service.action.schema.definition.DropIndex.Params(ctx.index_id)));
    }

}
