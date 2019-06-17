package org.service.concept.db.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.service.concept.db.event.RequestSelect;
import org.service.concept.db.event.ResponseForSelect;
import org.service.concept.db.event.Sorting;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class ExecuteSelect implements Execute<RequestSelect, ResponseForSelect> {

    static final String SELECT    = "SELECT ";
    static final String FROM      = NEW_LINE +
                                    "  FROM ";
    static final String WHERE     = NEW_LINE +
                                    " WHERE ";
    static final String ORDER_BY  = NEW_LINE +
                                    " ORDER BY ";
    static final String LIMIT     = NEW_LINE +
                                    " LIMIT ";
    static final String OFFSET    = NEW_LINE +
                                    "OFFSET ";
    static final String INDENT    = "       ";
    static final String INDENT_BY = "          ";

    static final String ASC       = " ASC";
    static final String DESC      = " DESC";

    @Override
    public ResponseForSelect execute(Connection db, RequestSelect request) throws SQLException {
        String sql = buildSql(request);
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            setValues(ps, request);
            try (ResultSet rs = ps.executeQuery()) {
                ImmutableList.Builder<ImmutableMap<String, Object>> records = ImmutableList.builder();
                while (rs.next()) {
                    records.add(getValues(rs, request));
                }
                return new ResponseForSelect(records.build());
            }
        }
    }

    String buildSql(RequestSelect request) {
        StringBuilder sb = new StringBuilder();
        sb.append(SELECT);
        sb.append(StringUtils.join(request.columns, COMMA_LINE + INDENT));
        sb.append(FROM);
        sb.append(request.table);
        sb.append(WHERE);
        buildConditionSql(sb, 7, false, request.condition, false);
        sb.append(ORDER_BY);

        String separator = COMMA_LINE + INDENT_BY;
        String sp        = EMPTY;
        for (Sorting sorting : request.sortings) {
            sb.append(sp);
            sb.append(sorting.column);
            sb.append(sorting.ascending ? ASC : DESC);
            sp = separator;
        }

        sb.append(LIMIT);
        sb.append(request.page.limit);
        sb.append(OFFSET);
        sb.append(request.page.offset);
        return sb.toString();
    }

    void setValues(PreparedStatement ps, RequestSelect request) throws SQLException {
        setConditionValues(ps, 1, request.condition);
    }

    ImmutableMap<String, Object> getValues(ResultSet rs, RequestSelect request) throws SQLException {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        int                                  index   = 1;
        for (String column : request.columns) {
            builder.put(column, rs.getObject(index++));
        }
        return builder.build();
    }
}
