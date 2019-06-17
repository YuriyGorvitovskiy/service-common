package org.service.concept.db.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.service.concept.db.event.RequestMerge;
import org.service.concept.db.event.ResponseForChange;

public class ExecuteMerge implements Execute<RequestMerge, ResponseForChange> {

    static final String INSERT_INTO = "INSERT INTO ";
    static final String COLUMNS     = " (";
    static final String VALUES      = ")" + NEW_LINE +
                                      "     VALUES (";
    static final String CONFLICT    = ")" + NEW_LINE +
                                      "ON CONFLICT (";
    static final String UPDATE      = ")" + NEW_LINE +
                                      "  DO UPDATE" + NEW_LINE +
                                      "        SET ";
    static final String INDENT      = "            ";
    static final String EXCLUDED    = "EXCLUDED.";

    @Override
    public ResponseForChange execute(Connection db, RequestMerge request) throws SQLException {
        String sql = buildSql(request);
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            setValues(ps, request);
            return new ResponseForChange(ps.executeUpdate());
        }
    }

    String buildSql(RequestMerge request) {
        StringBuilder sb = new StringBuilder();
        sb.append(INSERT_INTO);
        sb.append(request.table);
        sb.append(COLUMNS);
        sb.append(StringUtils.join(request.keys.keySet(), COMMA_SPACE));
        sb.append(COMMA_SPACE);
        sb.append(StringUtils.join(request.values.keySet(), COMMA_SPACE));
        sb.append(VALUES);
        sb.append(StringUtils.repeat(VAR, COMMA_SPACE, request.keys.size() + request.values.size()));
        sb.append(CONFLICT);
        sb.append(StringUtils.join(request.keys.keySet(), COMMA_SPACE));
        sb.append(UPDATE);
        String separator = COMMA_LINE + INDENT;
        String sp        = EMPTY;
        for (String column : request.values.keySet()) {
            sb.append(sp);
            sb.append(column);
            sb.append(EQ);
            sb.append(EXCLUDED);
            sb.append(column);
            sp = separator;
        }
        return sb.toString();
    }

    void setValues(PreparedStatement ps, RequestMerge request) throws SQLException {
        int index = 1;
        for (Object key : request.keys.values()) {
            ps.setObject(index++, key);
        }
        for (Object value : request.values.values()) {
            ps.setObject(index++, value);
        }
    }
}
