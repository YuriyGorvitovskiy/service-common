package org.service.action.schema.definition;

import org.service.action.Action;
import org.service.action.IAction;
import org.service.action.Result;
import org.service.action.Sequence;
import org.service.immutable.data.Patch;
import org.service.immutable.data.Patch.Operation;
import org.service.immutable.data.Row;
import org.service.immutable.schema.DataType;

import io.vavr.Tuple2;

@Action(service = "schema_manager", name = "create_column")
public class CreateColumn implements IAction<CreateColumn.Params, CreateColumn.Context> {

    public static class Params {
        public final Long     table_id;
        public final String   name;
        public final DataType type;

        Params(Long table_id, String name, DataType type) {
            this.table_id = table_id;
            this.name = name;
            this.type = type;
        }
    }

    public static class Context {
        @Sequence("column_id")
        public final Long new_column_id;

        Context(Long new_column_id) {
            this.new_column_id = new_column_id;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        return Result.of(new Patch(Operation.insert,
                Row.of("model",
                        "columns",
                        new Tuple2<>("id", ctx.new_column_id),
                        new Tuple2<>("table", params.table_id),
                        new Tuple2<>("name", params.name),
                        new Tuple2<>("type", params.type.name()))));
    }
}
