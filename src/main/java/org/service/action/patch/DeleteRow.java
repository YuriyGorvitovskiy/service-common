package org.service.action.patch;

import java.util.stream.Collectors;

import org.service.action.Action;
import org.service.action.Equal;
import org.service.action.From;
import org.service.action.IAction;
import org.service.action.Join;
import org.service.action.Key;
import org.service.action.Operand;
import org.service.action.Result;
import org.service.action.Select;
import org.service.action.Where;
import org.service.action.schema.Schema;
import org.service.action.schema.Schema.Columns;
import org.service.action.schema.Schema.Indexes;
import org.service.action.schema.Schema.Schemas;
import org.service.action.schema.Schema.Table;
import org.service.action.schema.Schema.Tables;
import org.service.dbc.DBConnection;
import org.service.immutable.data.Row;
import org.service.immutable.schema.DataType;

import io.vavr.collection.Map;
import io.vavr.collection.Set;

@Action(service = "persistence", name = "delete")
public class DeleteRow implements IAction<Row, DeleteRow.Context> {

    public static class Context {

        public final DBConnection dbc;

        @Select(alias = "c", value = Columns.TYPE)
        @Join({
                @From(schema = Schema.NAME, table = Table.COLUMNS, alias = "c"),
                @From(schema = Schema.NAME, table = Table.TABLES, alias = "t", on = @Equal(left = @Operand(alias = "t", column = Tables.ID), right = @Operand(alias = "c", column = Indexes.TABLE))),
                @From(schema = Schema.NAME, table = Table.SCHEMAS, alias = "s", on = @Equal(left = @Operand(alias = "s", column = Schemas.ID), right = @Operand(alias = "t", column = Tables.SCHEMA)))
        })
        @Where({
                @Equal(left = @Operand(alias = "t", column = Indexes.NAME), right = @Operand(param = "table")),
                @Equal(left = @Operand(alias = "s", column = Indexes.NAME), right = @Operand(param = "schema"))
        })
        @Key(alias = "c", value = Columns.NAME)
        public final Map<String, DataType> columns;

        Context(DBConnection dbc, Map<String, DataType> columns) {
            this.dbc = dbc;
            this.columns = columns;
        }

    }

    @Override
    public Result apply(Row params, Context ctx) {
        Set<String> columns = params.values.keySet();

        String dml = "DELETE FROM " + params.schema + "." + params.table +
                " WHERE " + columns.collect(Collectors.joining(" AND "));

        ctx.dbc.executeUpdate(dml, ps -> {
            int i = 1;
            for (String column : columns) {
                ctx.columns.get(column).get().set(ps, i++, params, column);
            }
        });
        return Result.empty;
    }

}
