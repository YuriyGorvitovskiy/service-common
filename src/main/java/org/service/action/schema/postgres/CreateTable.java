package org.service.action.schema.postgres;

import java.util.stream.Collectors;

import org.service.action.Action;
import org.service.action.IAction;
import org.service.action.Result;
import org.service.immutable.schema.DataType;

import io.vavr.collection.List;

@Action(service = "schema_manager", name = "create_table")
public class CreateTable implements IAction<CreateTable.Params, Context> {

    public static class Column {
        public final String   name;
        public final DataType type;

        Column(String name, DataType type) {
            this.name = name;
            this.type = type;
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

    public static class Params {
        public final String schema;
        public final String name;

        public final List<Column> columns;
        public final Index        primary;
        public final List<Index>  indexes;

        Params(String schema, String name, List<Column> columns, Index primary, List<Index> indexes) {
            this.schema = schema;
            this.name = name;
            this.columns = columns;
            this.primary = primary;
            this.indexes = indexes;
        }
    }

    public static class Schema {

        public final Long id;

        Schema(Long id) {
            this.id = id;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        CreateColumn createColumn = new CreateColumn();
        CreateIndex  createIndex  = new CreateIndex();
        String       ddl          = "CREATE TABLE " + params.schema + "." + params.name + "(" +
                params.columns
                    .map(c -> createColumn.columnDDL(new CreateColumn.Params(params.schema, params.name, c.name, c.type)))
                    .appendAll(null == params.primary
                            ? List.empty()
                            : List.of(params.primary)
                                .map(p -> createIndex
                                    .primaryDDL(new CreateIndex.Params(params.schema, params.name, p.name, true, p.columns))))
                    .collect(Collectors.joining(", "));
        ctx.dbc.execute(ddl);

        for (Index index : params.indexes) {
            CreateIndex.Params indexParams = new CreateIndex.Params(
                    params.schema,
                    params.name,
                    index.name,
                    false,
                    index.columns);
            ctx.dbc.execute(createIndex.indexDDL(indexParams));
        }

        return Result.empty;
    }

}
