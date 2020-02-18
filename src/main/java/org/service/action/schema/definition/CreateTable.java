package org.service.action.schema.definition;

import org.service.action.Action;
import org.service.action.ForEach;
import org.service.action.IAction;
import org.service.action.Key;
import org.service.action.Result;
import org.service.action.Sequence;
import org.service.immutable.data.Patch;
import org.service.immutable.data.Patch.Operation;
import org.service.immutable.data.Row;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;

@Action(service = "schema_manager", name = "create_table")
public class CreateTable implements IAction<CreateTable.Params, CreateTable.Context> {

    public static class Params {
        public final Long   schema_id;
        public final String name;

        public final List<CreateColumn.Params> columns;
        public final List<CreateIndex.Params>  indexes;

        Params(Long schema_id, String name, List<CreateColumn.Params> columns, List<CreateIndex.Params> indexes) {
            this.schema_id = schema_id;
            this.name = name;
            this.columns = columns;
            this.indexes = indexes;
        }
    }

    public static class Context {
        @Sequence("table_id")
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
                Row.of("model",
                        "tables",
                        new Tuple2<>("id", ctx.new_table_id),
                        new Tuple2<>("schema", params.schema_id),
                        new Tuple2<>("name", params.name))));

        final CreateColumn createColumn  = new CreateColumn();
        List<Patch>        columnPatches = params.columns
            .flatMap(p -> createColumn.apply(p, ctx.columns.get(p.name).get()).patches);

        final CreateIndex createIndex  = new CreateIndex();
        List<Patch>       indexPatches = params.indexes
            .flatMap(p -> createIndex.apply(p, ctx.indexes.get(p.name).get()).patches);

        return Result.ofPatches(tablePatches, columnPatches, indexPatches);
    }

}
