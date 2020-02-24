package org.service.action2.model.postgres;

import org.service.action2.Event;
import org.service.action2.Service;
import org.service.action2.model.IIndexDrop;

import io.vavr.collection.List;

@Service(Postgres.SERVICE)
public class IndexDrop implements IIndexDrop<Context> {

    @Override
    public List<Event<?>> apply(Params params, Context ctx) {
        String sql = "SELECT table_name "
                + "FROM information_schema.table_constraints "
                + "WHERE table_schema = ? AND constraint_name = ?";

        String pkTable = ctx.dbc.executeSingle(
                sql,
                ps -> {
                    ps.setString(1, params.schema);
                    ps.setString(2, params.name);
                },
                rs -> rs.getString(1));

        String ddl = null != pkTable
                ? "ALTER TABLE " + params.schema + "." + pkTable + " DROP CONSTRAINT " + params.name
                : "DROP INDEX " + params.schema + "." + params.name + " CASCADE";

        ctx.dbc.execute(ddl);

        return List.empty();
    }

}
