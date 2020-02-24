package org.service.database.postgres.action.model;

import org.service.action2.Event;
import org.service.action2.Service;
import org.service.action2.model.ISchemaDrop;
import org.service.action2.model.ITableDrop;
import org.service.database.postgres.ServicePostgres;
import org.service.immutable.schema.DataType;
import org.service.sql.simple.Equal;
import org.service.sql.simple.Select;

import io.vavr.collection.List;

@Service(ServicePostgres.NAME)
public class SchemaDrop implements ISchemaDrop<Context> {

    final TableDrop tableAction = new TableDrop();

    @Override
    public List<Event<?>> apply(Params params, Context ctx) {
        // Schema could contains a lot of tables,
        // In this case DROP SCHEMA could exceed transaction limit.
        // So let us drop all tables individually.
        List<String> tables = getTables(params, ctx);
        for (String table : tables) {
            tableAction.apply(new ITableDrop.Params(params.name, table), ctx);
        }

        String ddl = "DROP SCHEMA " + params.name + " CASCADE";
        ctx.dbc.execute(ddl);

        return List.empty();

    }

    List<String> getTables(Params params, Context ctx) {
        Select select = new Select("table_name")
            .from("information_schema", "tables")
            .where(Equal.of("table_schema", DataType.LABEL, params.name));

        return ctx.dbc.executeQuery(
                select.sql(),
                select.bind(),
                rs -> rs.getString(1));
    }

}
