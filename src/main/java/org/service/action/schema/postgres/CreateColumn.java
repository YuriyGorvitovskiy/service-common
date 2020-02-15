package org.service.action.schema.postgres;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.service.action.Action;
import org.service.action.IAction;
import org.service.action.Result;
import org.service.immutable.schema.DataType;

@Action(service = "schema_manager", name = "create_column")
public class CreateColumn implements IAction<CreateColumn.Params, Context> {

    public static class Params {
        public final String   schema;
        public final String   table;
        public final String   name;
        public final DataType type;

        public Params(String schema, String table, String name, DataType type) {
            this.schema = schema;
            this.table = table;
            this.name = name;
            this.type = type;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        String ddl = "ALTER TABLE " + params.schema + "." + params.table + " ADD COLUMN " + columnDDL(params);
        try (PreparedStatement ps = ctx.dbc.prepareStatement(ddl)) {
            ps.execute();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return Result.empty;

    }

    public String columnDDL(Params params) {
        return params.name + " " + params.type.postgres();
    }
}
