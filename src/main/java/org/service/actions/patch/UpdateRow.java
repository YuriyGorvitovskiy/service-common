package org.service.actions.patch;

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
import org.service.action.schema.Schema.IndexColumns;
import org.service.action.schema.Schema.Indexes;
import org.service.action.schema.Schema.Schemas;
import org.service.action.schema.Schema.Table;
import org.service.action.schema.Schema.Tables;
import org.service.dbc.DBConnection;
import org.service.immutable.data.Row;
import org.service.immutable.schema.DataType;

import io.vavr.collection.Map;
import io.vavr.collection.Set;

@Action(service = "persistence", name = "update")
public class UpdateRow implements IAction<Row, UpdateRow.Context> {

    public static class Context {

        public final DBConnection dbc;

        @Select(alias = "c", value = Columns.NAME)
        @Join({
                @From(schema = Schema.NAME, table = Table.SCHEMAS, alias = "s"),
                @From(schema = Schema.NAME, table = Table.TABLES, alias = "t", on = @Equal(left = @Operand(alias = "t", column = Tables.SCHEMA), right = @Operand(alias = "s", column = Schemas.ID))),
                @From(schema = Schema.NAME, table = Table.INDEXES, alias = "i", on = @Equal(left = @Operand(alias = "i", column = Indexes.TABLE), right = @Operand(alias = "t", column = Tables.ID))),
                @From(schema = Schema.NAME, table = Table.INDEX_COLUMNS, alias = "ic", on = @Equal(left = @Operand(alias = "ic", column = IndexColumns.INDEX), right = @Operand(alias = "i", column = Indexes.ID))),
                @From(schema = Schema.NAME, table = Table.COLUMNS, alias = "c", on = @Equal(left = @Operand(alias = "c", column = Columns.ID), right = @Operand(alias = "ic", column = IndexColumns.COLUMN)))
        })
        @Where({
                @Equal(left = @Operand(alias = "s", column = Schemas.NAME), right = @Operand(param = "schema")),
                @Equal(left = @Operand(alias = "t", column = Tables.NAME), right = @Operand(param = "table")),
                @Equal(left = @Operand(alias = "i", column = Indexes.PRIMARY), right = @Operand(value = "true"))
        })
        public final Set<String> primary;

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

        Context(DBConnection dbc, Set<String> primary, Map<String, DataType> columns) {
            this.dbc = dbc;
            this.primary = primary;
            this.columns = columns;
        }

    }

    @Override
    public Result apply(Row params, Context ctx) {

        Set<String> columns = ctx.columns.keySet()
            .filter(c -> ctx.primary.contains(c));

        String dml = "UPDATE " + params.schema + "." + params.table + " SET " +
                columns.map(c -> c + " = ?").collect(Collectors.joining(", ")) +
                " WHERE " + ctx.primary.map(c -> c + " = ?").collect(Collectors.joining(" AND "));

        ctx.dbc.executeUpdate(dml, ps -> {
            int i = 1;
            for (String column : columns) {
                ctx.columns.get(column).get().set(ps, i++, params, column);
            }
            for (String column : ctx.primary) {
                ctx.columns.get(column).get().set(ps, i++, params, column);
            }
        });
        return Result.empty;
    }

}
