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
import org.service.action.schema.Schema.Columns;
import org.service.action.schema.Schema.Indexes;
import org.service.action.schema.Schema.Schemas;
import org.service.action.schema.Schema.Table;
import org.service.action.schema.Schema.Tables;

@Action(service = Service.SCHEMA, name = Service.Create.COLUMN)
public class DropColumn implements IAction<DropColumn.Params, DropColumn.Context> {

    public static class Params {
        public final String schema;
        public final String table;
        public final String column;

        public Params(String schema,
                      String table,
                      String column) {
            this.schema = schema;
            this.table = table;
            this.column = column;
        }
    }

    public static class Context {
        @Select(alias = "c", value = "id")
        @Join({
                @From(schema = Schema.NAME, table = Table.COLUMNS, alias = "c"),
                @From(schema = Schema.NAME, table = Table.TABLES, alias = "t", on = @Equal(left = @Operand(alias = "t", column = Tables.ID), right = @Operand(alias = "c", column = Indexes.TABLE))),
                @From(schema = Schema.NAME, table = Table.SCHEMAS, alias = "s", on = @Equal(left = @Operand(alias = "s", column = Schemas.ID), right = @Operand(alias = "t", column = Tables.SCHEMA)))
        })
        @Where({
                @Equal(left = @Operand(alias = "c", column = Columns.NAME), right = @Operand(param = "column")),
                @Equal(left = @Operand(alias = "t", column = Tables.NAME), right = @Operand(param = "table")),
                @Equal(left = @Operand(alias = "s", column = Schemas.NAME), right = @Operand(param = "schema"))
        })
        public final Long column_id;

        public Context(Long column_id) {
            this.column_id = column_id;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        return Result.of(
                new Event<>(
                        Service.POSTGRES,
                        Service.Drop.COLUMN,
                        new org.service.action.schema.postgres.DropColumn.Params(
                                params.schema,
                                params.table,
                                params.column)),
                new Event<>(
                        Service.DEFINITION,
                        Service.Drop.COLUMN,
                        new org.service.action.schema.definition.DropColumn.Params(ctx.column_id)));
    }

}
