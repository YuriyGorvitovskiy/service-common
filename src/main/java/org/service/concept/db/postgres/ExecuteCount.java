package org.service.concept.db.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.service.concept.db.event.RequestCount;
import org.service.concept.db.event.ResponseForCount;

public class ExecuteCount implements Execute<RequestCount, ResponseForCount> {
    static final String SELECT_COUNT_FROM = "SELECT COUNT(*)" + NEW_LINE +
                                            "  FROM ";
    static final String WHERE             = " WHERE ";

    @Override
    public ResponseForCount execute(Connection db, RequestCount request) throws SQLException {
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
                return new ResponseForCount(count);
            }
        }

    }

    String buildSql(RequestCount request) {
        StringBuilder sb = new StringBuilder();
        sb.append(SELECT_COUNT_FROM);
        sb.append(request.table);
        sb.append(NEW_LINE);
        sb.append(WHERE);

        buildConditionSql(sb, 7, false, request.condition, false);
        return sb.toString();
    }

    void setValues(PreparedStatement ps, RequestCount request) throws SQLException {
        setConditionValues(ps, 1, request.condition);
    }
}
