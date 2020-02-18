package org.service.action.schema;

import org.service.action.Action;
import org.service.action.Equal;
import org.service.action.Event;
import org.service.action.From;
import org.service.action.IAction;
import org.service.action.Result;
import org.service.action.Select;
import org.service.action.Where;
import org.service.action.schema.Schema.Columns;
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
        @From(schema = Schema.NAME, table = Table.SCHEMAS)
        @Where({ @Equal(column = Schemas.NAME, param = "schema") })
        public final SchemaCtx schema;

        public Context(SchemaCtx schema) {
            this.schema = schema;
        }
    }

    public static class SchemaCtx {

        @Select(Schemas.ID)
        Long id;

        @Select(Columns.ID)
        @From(schema = Schema.NAME, table = Table.TABLES)
        @Where({
                @Equal(column = Tables.SCHEMA, context = "id"),
                @Equal(column = Tables.NAME, param = "table")
        })
        public final Long table_id;

        public SchemaCtx(Long id, Long table_id) {
            this.id = id;
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
                                ctx.schema.table_id,
                                params.name,
                                params.type)));
    }

}
