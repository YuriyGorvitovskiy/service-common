package org.service.command.dml.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.service.command.dml.UpdateResult;
import org.service.command.dml.UpsertParams;

public class Upsert implements DMLCommand<UpsertParams, UpdateResult> {

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
    public UpdateResult apply(UpsertParams request, Connection db) {
        String sql = buildSql(request);
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            setValues(ps, request);
            return new UpdateResult(ps.executeUpdate());
        } catch (SQLException ex) {
            throw new RuntimeException(sql, ex);
        }
    }

    String buildSql(UpsertParams request) {
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

    void setValues(PreparedStatement ps, UpsertParams request) throws SQLException {
        int index = 1;
        for (Object key : request.keys.values()) {
            ps.setObject(index++, key);
        }
        for (Object value : request.values.values()) {
            ps.setObject(index++, value);
        }
    }
}
