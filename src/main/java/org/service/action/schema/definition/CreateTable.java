package org.service.action.schema.definition;

import org.service.action.Action;
import org.service.action.ForEach;
import org.service.action.IAction;
import org.service.action.Key;
import org.service.action.Result;
import org.service.action.Sequence;
import org.service.action.schema.Schema;
import org.service.action.schema.Schema.Table;
import org.service.action.schema.Schema.Tables;
import org.service.action.schema.Service;
import org.service.immutable.data.Patch;
import org.service.immutable.data.Patch.Operation;
import org.service.immutable.data.Row;
import org.service.immutable.schema.DataType;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;

@Action(service = Service.DEFINITION, name = Service.Create.TABLE)
public class CreateTable implements IAction<CreateTable.Params, CreateTable.Context> {

    public static class Params {
        public final Long   schema_id;
        public final String name;

        public final List<Column> columns;
        public final List<Index>  indexes;

        public Params(Long schema_id, String name, List<Column> columns, List<Index> indexes) {
            this.schema_id = schema_id;
            this.name = name;
            this.columns = columns;
            this.indexes = indexes;
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

    public static class Index {
        public final String       name;
        public final Boolean      primary;
        public final List<String> columns;

        public Index(String name, Boolean primary, List<String> columns) {
            this.name = name;
            this.primary = primary;
            this.columns = columns;
        }
    }

    public static class Context {
        @Sequence(Schema.Sequence.TABLE_ID)
        public final Long new_table_id;

        @ForEach(param = "columns")
        @Key("name")
        public final Map<String, CreateColumn.Context> columns;

        @ForEach(param = "indexes")
        @Key("name")
        public final Map<String, CreateIndex.Context> indexes;

        Context(Long new_table_id,
                Map<String, CreateColumn.Context> new_column_ids,
                Map<String, CreateIndex.Context> new_index_ids) {
            this.new_table_id = new_table_id;
            this.columns = new_column_ids;
            this.indexes = new_index_ids;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        List<Patch> tablePatches = List.of(new Patch(Operation.insert,
                Row.of(Schema.NAME,
                        Table.TABLES,
                        new Tuple2<>(Tables.ID, ctx.new_table_id),
                        new Tuple2<>(Tables.SCHEMA, params.schema_id),
                        new Tuple2<>(Tables.NAME, params.name))));

        final CreateColumn createColumn  = new CreateColumn();
        List<Patch>        columnPatches = params.columns
            .flatMap(p -> createColumn.apply(
                    new CreateColumn.Params(ctx.new_table_id, p.name, p.type),
                    ctx.columns.get(p.name).get()).patches);

        final CreateIndex createIndex  = new CreateIndex();
        List<Patch>       indexPatches = params.indexes
            .flatMap(p -> createIndex.apply(
                    new CreateIndex.Params(
                            ctx.new_table_id,
                            p.name,
                            p.primary,
                            p.columns.map(c -> ctx.columns.get(c).get().new_column_id)),
                    ctx.indexes.get(p.name).get()).patches);

        return Result.ofPatches(tablePatches, columnPatches, indexPatches);
    }

}
