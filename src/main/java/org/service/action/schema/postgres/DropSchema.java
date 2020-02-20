package org.service.action.schema.postgres;

import org.service.action.Action;
import org.service.action.IAction;
import org.service.action.Result;
import org.service.action.schema.Service;
import org.service.immutable.schema.DataType;
import org.service.sql.simple.Equal;
import org.service.sql.simple.Select;

import io.vavr.collection.List;

@Action(service = Service.POSTGRES, name = Service.Drop.SCHEMA)
public class DropSchema implements IAction<DropSchema.Params, Context> {

    public static class Params {
        public final String name;

        public Params(String name) {
            this.name = name;
        }
    }

    DropTable action = new DropTable();

    @Override
    public Result apply(Params params, Context ctx) {
        // Schema could contains a lot of tables,
        // In this case DROP SCHEMA could exceed transaction limit.
        // So let us drop all tables individually.
        List<String> tables = getTables(params, ctx);
        for (String table : tables) {
            action.apply(new DropTable.Params(params.name, table), ctx);
        }

        String ddl = "DROP SCHEMA " + params.name + " CASCADE";
        ctx.dbc.execute(ddl);

        return Result.empty;
    }

    public List<String> getTables(Params params, Context ctx) {
        Select select = new Select("table_name")
            .from("information_schema", "tables")
            .where(Equal.of("table_schema", DataType.LABEL, params.name));

        return ctx.dbc.executeQuery(
                select.sql(),
                select.bind(),
                rs -> rs.getString(1));
    }
}
