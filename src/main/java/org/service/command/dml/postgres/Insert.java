package org.service.command.dml.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.service.command.dml.InsertParams;
import org.service.command.dml.UpdateResult;

public class Insert implements DMLCommand<InsertParams, UpdateResult> {
    static final String INSERT_INTO = "INSERT INTO ";
    static final String COLUMNS     = " (";
    static final String VALUES      = ")" + NEW_LINE +
            "VALUES (";

    @Override
    public UpdateResult apply(InsertParams request, Connection db) {
        String sql = buildSql(request);
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            setValues(ps, request);
            return new UpdateResult(ps.executeUpdate());
        } catch (SQLException ex) {
            throw new RuntimeException(sql, ex);
        }
    }

    String buildSql(InsertParams request) {
        StringBuilder sb = new StringBuilder();
        sb.append(INSERT_INTO);
        sb.append(request.table);
        sb.append(COLUMNS);
        sb.append(StringUtils.join(request.values.keySet(), COMMA_SPACE));
        sb.append(VALUES);
        sb.append(StringUtils.repeat(VAR, COMMA_SPACE, request.values.size()));
        sb.append(CLOSE);
        return sb.toString();
    }

    void setValues(PreparedStatement ps, InsertParams request) throws SQLException {
        int index = 1;
        for (Object value : request.values.values()) {
            ps.setObject(index++, value);
        }
    }
}
