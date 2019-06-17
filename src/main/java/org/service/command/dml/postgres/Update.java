package org.service.command.dml.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.service.command.dml.UpdateParams;
import org.service.command.dml.UpdateResult;

public class Update implements DMLCommand<UpdateParams, UpdateResult> {
    static final String UPDATE = "UPDATE ";
    static final String SET    = "   SET ";
    static final String WHERE  = " WHERE ";
    static final String INDENT = "       ";

    @Override
    public UpdateResult apply(UpdateParams request, Connection db) {
        String sql = buildSql(request);
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            setValues(ps, request);
            return new UpdateResult(ps.executeUpdate());
        } catch (SQLException ex) {
            throw new RuntimeException(sql, ex);
        }
    }

    String buildSql(UpdateParams request) {
        StringBuilder sb = new StringBuilder();
        sb.append(UPDATE);
        sb.append(request.table);
        sb.append(NEW_LINE);
        sb.append(SET);
        String separator = COMMA_LINE + INDENT;
        String sp        = EMPTY;
        for (String column : request.values.keySet()) {
            sb.append(sp);
            sb.append(column);
            sb.append(EQ);
            sb.append(VAR);
            sp = separator;
        }
        sb.append(NEW_LINE);
        sb.append(WHERE);
        sb.append(buildConditionSql(7, false, request.filter, false));
        return sb.toString();
    }

    void setValues(PreparedStatement ps, UpdateParams request) throws SQLException {
        int index = 1;
        for (Object value : request.values.values()) {
            ps.setObject(index++, value);
        }
        setConditionValues(ps, index, request.filter);
    }
}
