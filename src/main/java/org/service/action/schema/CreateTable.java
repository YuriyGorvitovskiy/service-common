package org.service.action.schema;

import org.service.action.Action;
import org.service.action.Equal;
import org.service.action.Event;
import org.service.action.From;
import org.service.action.Operand;
import org.service.action.Result;
import org.service.action.Select;
import org.service.action.Where;
import org.service.action.schema.Schema.Schemas;

import io.vavr.Tuple2;
import io.vavr.collection.List;

@Action(service = Service.SCHEMA, name = Service.Create.TABLE)
public class CreateTable {

    public static class Params extends Table {
        public final String schema;

        public Params(String schema,
                      String name,
                      List<CreateColumn.Column> columns,
                      CreateIndex.Index primary,
                      List<CreateIndex.Index> indexes) {
            super(name, columns, primary, indexes);
            this.schema = schema;
        }
    }

    public static class Table {
        public final String name;

        public final List<CreateColumn.Column> columns;
        public final CreateIndex.Index         primary;
        public final List<CreateIndex.Index>   indexes;

        public Table(String name,
                     List<CreateColumn.Column> columns,
                     CreateIndex.Index primary,
                     List<CreateIndex.Index> indexes) {
            this.name = name;
            this.columns = columns;
            this.primary = primary;
            this.indexes = indexes;
        }
    }

    public static class Context {
        @Select(Schemas.ID)
        @From(schema = Schema.NAME, table = Schema.Table.SCHEMAS)
        @Where({ @Equal(left = @Operand(column = Schemas.NAME), right = @Operand(param = "schema")) })
        public final Long schema_id;

        public Context(Long schema_id) {
            this.schema_id = schema_id;
        }

    }

    public Result apply(Params params, Context ctx) {
        return Result.of(
                new Event<>(
                        Service.POSTGRES,
                        Service.Create.TABLE,
                        new org.service.action.schema.postgres.CreateTable.Params(
                                params.schema,
                                params.name,
                                params.columns
                                    .map(c -> new org.service.action.schema.postgres.CreateTable.Column(
                                            c.name,
                                            c.type)),
                                null == params.primary ? null
                                        : new org.service.action.schema.postgres.CreateTable.Index(
                                                params.primary.name,
                                                params.primary.columns),
                                params.indexes
                                    .map(i -> new org.service.action.schema.postgres.CreateTable.Index(
                                            i.name,
                                            i.columns)))),
                new Event<>(
                        Service.DEFINITION,
                        Service.Create.TABLE,
                        new org.service.action.schema.definition.CreateTable.Params(
                                ctx.schema_id,
                                params.name,
                                params.columns
                                    .map(c -> new org.service.action.schema.definition.CreateTable.Column(
                                            c.name,
                                            c.type)),
                                joinIndexes(params)
                                    .map(i -> new org.service.action.schema.definition.CreateTable.Index(
                                            i._1.name,
                                            i._2,
                                            i._1.columns)))));
    }

    List<Tuple2<CreateIndex.Index, Boolean>> joinIndexes(Params params) {
        return null == params.primary ? List.empty()
                : List.of(new Tuple2<>(params.primary, true))
                    .appendAll(params.indexes.map(i -> new Tuple2<>(i, false)));
    }

}
