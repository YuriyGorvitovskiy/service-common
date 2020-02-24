package org.service.database.postgres.action.patch;

import java.util.stream.Collectors;

import org.service.action2.Event;
import org.service.action2.Service;
import org.service.action2.patch.IDelete;
import org.service.database.postgres.ServicePostgres;
import org.service.immutable.data.Row;

import io.vavr.collection.List;
import io.vavr.collection.Set;

@Service(ServicePostgres.NAME)
public class Delete implements IDelete<Context> {

    @Override
    public List<Event<?>> apply(Row params, Context ctx) {
        Set<String> columns = params.values.keySet();

        String dml = "DELETE FROM " + params.schema + "." + params.table +
                " WHERE " + columns.map(c -> c + " = ?").collect(Collectors.joining(" AND "));

        ctx.dbc.executeUpdate(dml, ps -> {
            int i = 1;
            for (String column : columns) {
                ctx.columnsType.get(column).get().set(ps, i++, params, column);
            }
        });

        return List.empty();
    }
}
