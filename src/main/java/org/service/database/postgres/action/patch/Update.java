package org.service.database.postgres.action.patch;

import java.util.stream.Collectors;

import org.service.action2.Event;
import org.service.action2.Service;
import org.service.action2.patch.IUpdate;
import org.service.database.postgres.ServicePostgres;
import org.service.immutable.data.Row;

import io.vavr.collection.List;
import io.vavr.collection.Set;

@Service(ServicePostgres.NAME)
public class Update implements IUpdate<Context> {

    @Override
    public List<Event<?>> apply(Row params, Context ctx) {
        Set<String> columns = ctx.tableInfo.columnsType.keySet()
            .filter(c -> !ctx.tableInfo.primaryColumns.contains(c));

        String dml = "UPDATE " + params.schema + "." + params.table + " SET " +
                columns.map(c -> c + " = ?").collect(Collectors.joining(", ")) +
                " WHERE " + ctx.tableInfo.primaryColumns.map(c -> c + " = ?").collect(Collectors.joining(" AND "));

        ctx.dbc.executeUpdate(dml, ps -> {
            int i = 1;
            for (String column : columns) {
                ctx.tableInfo.columnsType.get(column).get().set(ps, i++, params, column);
            }
            for (String column : ctx.tableInfo.primaryColumns) {
                ctx.tableInfo.columnsType.get(column).get().set(ps, i++, params, column);
            }
        });

        return List.empty();
    }
}
