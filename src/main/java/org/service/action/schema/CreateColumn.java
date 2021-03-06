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
import org.service.immutable.schema.DataType;

@Action(service = Service.SCHEMA, name = Service.Create.COLUMN)
public class CreateColumn implements IAction<CreateColumn.Params, CreateColumn.Context> {

    public static class Params extends Column {
        public final String schema;
        public final String table;

        public Params(String schema,
                      String table,
                      String name,
                      DataType type) {
            super(name, type);
            this.schema = schema;
            this.table = table;
        }
    }

    public static class Column {
        public final String   name;
        public final DataType type;

        public Column(String name, DataType type) {
            this.name = name;
            this.type = type;
        }
    }

    public static class Context {
        @Select(alias = "c", value = "id")
        @Join({
                @From(schema = Schema.NAME, table = Table.TABLES, alias = "t"),
                @From(schema = Schema.NAME, table = Table.SCHEMAS, alias = "s", on = @Equal(left = @Operand(alias = "s", column = Schemas.ID), right = @Operand(alias = "t", column = Tables.SCHEMA)))
        })
        @Where({
                @Equal(left = @Operand(alias = "t", column = Indexes.NAME), right = @Operand(param = "table")),
                @Equal(left = @Operand(alias = "s", column = Indexes.NAME), right = @Operand(param = "schema"))
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
                        Service.Create.COLUMN,
                        new org.service.action.schema.postgres.CreateColumn.Params(
                                params.schema,
                                params.table,
                                params.name,
                                params.type)),
                new Event<>(
                        Service.DEFINITION,
                        Service.Create.COLUMN,
                        new org.service.action.schema.definition.CreateColumn.Params(
                                ctx.table_id,
                                params.name,
                                params.type)));
    }

}
