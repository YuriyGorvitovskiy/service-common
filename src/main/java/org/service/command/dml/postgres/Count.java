package org.service.command.dml.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.service.command.dml.CountParams;
import org.service.command.dml.CountResult;

public class Count implements DMLCommand<CountParams, CountResult> {
    static final String SELECT_COUNT_FROM = "SELECT COUNT(*)" + NEW_LINE +
            "  FROM ";
    static final String WHERE             = " WHERE ";

    @Override
    public CountResult apply(CountParams request, Connection db) {
        String sql = buildSql(request);
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            setValues(ps, request);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("No rows for COUNT(*) query:\n" + sql);
                }
                long count = rs.getLong(1);
                if (rs.next()) {
                    throw new SQLException("More then one row for COUNT(*) query:\n" + sql);
                }
                return new CountResult(count);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(sql, ex);
        }

    }

    String buildSql(CountParams request) {
        StringBuilder sb = new StringBuilder();
        sb.append(SELECT_COUNT_FROM);
        sb.append(request.table);
        sb.append(NEW_LINE);
        sb.append(WHERE);

        sb.append(buildConditionSql(7, false, request.filter, false));
        return sb.toString();
    }

    void setValues(PreparedStatement ps, CountParams request) throws SQLException {
        setConditionValues(ps, 1, request.filter);
    }
}
