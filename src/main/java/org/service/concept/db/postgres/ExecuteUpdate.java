package org.service.concept.db.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.service.concept.db.event.RequestUpdate;
import org.service.concept.db.event.ResponseForChange;

public class ExecuteUpdate implements Execute<RequestUpdate, ResponseForChange> {
    static final String UPDATE = "UPDATE ";
    static final String SET    = "   SET ";
    static final String WHERE  = " WHERE ";
    static final String INDENT = "       ";

    @Override
    public ResponseForChange execute(Connection db, RequestUpdate request) throws SQLException {
        String sql = buildSql(request);
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            setValues(ps, request);
            return new ResponseForChange(ps.executeUpdate());
        }
    }

    String buildSql(RequestUpdate request) {
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
        buildConditionSql(sb, 7, false, request.condition, false);
        return sb.toString();
    }

    void setValues(PreparedStatement ps, RequestUpdate request) throws SQLException {
        int index = 1;
        for (Object value : request.values.values()) {
            ps.setObject(index++, value);
        }
        setConditionValues(ps, index, request.condition);
    }
}
