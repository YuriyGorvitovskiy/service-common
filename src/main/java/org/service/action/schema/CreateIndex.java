package org.service.action.schema;

import org.service.action.Action;
import org.service.action.Equal;
import org.service.action.Event;
import org.service.action.From;
import org.service.action.IAction;
import org.service.action.Key;
import org.service.action.Operand;
import org.service.action.Result;
import org.service.action.Select;
import org.service.action.Where;
import org.service.action.schema.Schema.Columns;
import org.service.action.schema.Schema.Schemas;
import org.service.action.schema.Schema.Table;
import org.service.action.schema.Schema.Tables;

import io.vavr.collection.List;
import io.vavr.collection.Map;

@Action(service = Service.SCHEMA, name = Service.Create.INDEX)
public class CreateIndex implements IAction<CreateIndex.Params, CreateIndex.Context> {

    public static class Params extends Index {
        public final String  schema;
        public final String  table;
        public final Boolean primary;

        public Params(String schema, String table, String name, Boolean primary, List<String> columns) {
            super(name, columns);
            this.schema = schema;
            this.table = table;
            this.primary = primary;
        }
    }

    public static class Index {
        public final String       name;
        public final List<String> columns;

        Index(String name, List<String> columns) {
            this.name = name;
            this.columns = columns;
        }
    }

    public static class Context {
        @From(schema = Schema.NAME, table = Table.SCHEMAS)
        @Where({ @Equal(left = @Operand(column = Schemas.NAME), right = @Operand(param = "schema")) })
        public final SchemaContext schema;

        public Context(SchemaContext schema) {
            this.schema = schema;
        }
    }

    public static class SchemaContext {

        @Select(Schemas.ID)
        public final Long id;

        @From(schema = Schema.NAME, table = Table.TABLES)
        @Where({
                @Equal(left = @Operand(column = Tables.SCHEMA), right = @Operand(context = "id")),
                @Equal(left = @Operand(column = Tables.NAME), right = @Operand(param = "table"))
        })
        public final TableContext table;

        public SchemaContext(Long id, TableContext table) {
            this.id = id;
            this.table = table;
        }
    }

    public static class TableContext {
        @Select(Schemas.ID)
        public final Long id;

        @Select(Columns.ID)
        @From(schema = Schema.NAME, table = Table.COLUMNS)
        @Where({
                @Equal(left = @Operand(column = Columns.TABLE), right = @Operand(context = "id")),
                @Equal(left = @Operand(column = Columns.NAME), right = @Operand(param = "columns"))
        })
        @Key(Columns.NAME)
        public final Map<String, Long> column_ids;

        public TableContext(Long id, Map<String, Long> column_ids) {
            this.id = id;
            this.column_ids = column_ids;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        return Result.of(
                new Event<>(
                        Service.POSTGRES,
                        Service.Create.INDEX,
                        new org.service.action.schema.postgres.CreateIndex.Params(
                                params.schema,
                                params.table,
                                params.name,
                                params.primary,
                                params.columns)),
                new Event<>(
                        Service.DEFINITION,
                        Service.Create.INDEX,
                        new org.service.action.schema.definition.CreateIndex.Params(
                                ctx.schema.table.id,
                                params.name,
                                params.primary,
                                params.columns.map(c -> ctx.schema.table.column_ids.get(c).get()))));
    }
}
