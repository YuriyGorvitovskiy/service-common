package org.service.action.schema.definition;

import org.service.action.Action;
import org.service.action.Equal;
import org.service.action.From;
import org.service.action.IAction;
import org.service.action.Operand;
import org.service.action.Result;
import org.service.action.Where;
import org.service.action.schema.Schema;
import org.service.action.schema.Schema.Columns;
import org.service.action.schema.Schema.Indexes;
import org.service.action.schema.Schema.Table;
import org.service.action.schema.Schema.Tables;
import org.service.action.schema.Service;
import org.service.immutable.data.Patch;
import org.service.immutable.data.Patch.Operation;
import org.service.immutable.data.Row;

import io.vavr.Tuple2;
import io.vavr.collection.List;

@Action(service = Service.DEFINITION, name = Service.Drop.TABLE)
public class DropTable implements IAction<DropTable.Params, DropTable.Context> {

    public static class Params {
        public final Long id;

        public Params(Long id) {
            this.id = id;
        }
    }

    public static class Context {

        @From(schema = Schema.NAME, table = Table.COLUMNS)
        @Where({ @Equal(left = @Operand(column = Columns.TABLE), right = @Operand(param = "id")) })
        public final List<DropColumn.Params> columns;

        @From(schema = Schema.NAME, table = Table.INDEXES)
        @Where({ @Equal(left = @Operand(column = Indexes.TABLE), right = @Operand(param = "id")) })
        public final List<DropIndex.Params> indexes;

        Context(List<DropIndex.Params> indexes, List<DropColumn.Params> columns) {
            this.indexes = indexes;
            this.columns = columns;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        List<Patch> tablePatches = List.of(new Patch(Operation.delete,
                Row.of(Schema.NAME,
                        Table.TABLES,
                        new Tuple2<>(Tables.ID, params.id))));

        DropColumn  dropColumn    = new DropColumn();
        List<Patch> columnPatches = ctx.columns.flatMap(p -> dropColumn.columnPatches(p));

        DropIndex   dropIndex    = new DropIndex();
        List<Patch> indexPatches = ctx.indexes.flatMap(p -> dropIndex.apply(p, new DropIndex.Context()).patches);

        return Result.ofPatches(indexPatches, columnPatches, tablePatches);
    }
}
