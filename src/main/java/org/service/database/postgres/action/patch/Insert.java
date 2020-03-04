package org.service.database.postgres.action.patch;

import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.service.action2.Event;
import org.service.action2.Service;
import org.service.action2.patch.IInsert;
import org.service.database.postgres.ServicePostgres;
import org.service.immutable.data.Row;

import io.vavr.collection.List;
import io.vavr.collection.Set;

@Service(ServicePostgres.NAME)
public class Insert implements IInsert<Context> {

    @Override
    public List<Event<?>> apply(Row params, Context ctx) {
        Set<String> columns = params.values.keySet();

        String dml = "INSERT INTO " + params.schema + "." + params.table + "(" +
                columns.collect(Collectors.joining(", ")) +
                ") VALUES (" + StringUtils.repeat("?", ", ", columns.size()) + ")";

        ctx.dbc.executeUpdate(dml, ps -> {
            int i = 1;
            for (String column : columns) {
                ctx.tableInfo.columnsType.get(column).get().set(ps, i++, params, column);
            }
        });

        return List.empty();
    }
}
