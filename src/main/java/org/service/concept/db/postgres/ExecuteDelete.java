package org.service.concept.db.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.service.concept.db.event.RequestDelete;
import org.service.concept.db.event.ResponseForChange;

public class ExecuteDelete implements Execute<RequestDelete, ResponseForChange> {
    static final String DELETE_FROM = "DELETE FROM ";
    static final String WHERE       = NEW_LINE +
                                      " WHERE ";

    @Override
    public ResponseForChange execute(Connection db, RequestDelete request) throws SQLException {
        String sql = buildSql(request);
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            setValues(ps, request);
            return new ResponseForChange(ps.executeUpdate());
        }
    }

    String buildSql(RequestDelete request) {
        StringBuilder sb = new StringBuilder();
        sb.append(DELETE_FROM);
        sb.append(request.table);
        sb.append(WHERE);

        buildConditionSql(sb, 7, false, request.condition, false);
        return sb.toString();
    }

    void setValues(PreparedStatement ps, RequestDelete request) throws SQLException {
        setConditionValues(ps, 1, request.condition);
    }
}
