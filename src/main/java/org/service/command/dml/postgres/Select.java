package org.service.command.dml.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.service.command.dml.SelectParams;
import org.service.command.dml.SelectResult;
import org.service.command.dml.predicate.OrderBy;

import io.vavr.Tuple;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.collection.Stream;
import io.vavr.collection.Vector;
import io.vavr.control.Option;

public class Select implements DMLCommand<SelectParams, SelectResult> {

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
    public SelectResult apply(SelectParams request, Connection db) {
        String sql = buildSql(request);
        try (PreparedStatement ps = db.prepareStatement(sql)) {
            setValues(ps, request);
            try (ResultSet rs = ps.executeQuery()) {
                return new SelectResult(getResult(rs, request));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(sql, ex);
        }
    }

    String buildSql(SelectParams request) {
        StringBuilder sb = new StringBuilder();
        sb.append(SELECT);
        sb.append(StringUtils.join(request.columns, COMMA_LINE + INDENT));
        sb.append(FROM);
        sb.append(request.table);
        sb.append(WHERE);
        sb.append(buildConditionSql(7, false, request.filter, false));
        sb.append(ORDER_BY);

        String separator = COMMA_LINE + INDENT_BY;
        String sp        = EMPTY;
        for (OrderBy sorting : request.orderBy) {
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

    void setValues(PreparedStatement ps, SelectParams request) throws SQLException {
        setConditionValues(ps, 1, request.filter);
    }

    Seq<Map<String, Object>> getResult(ResultSet rs, SelectParams request) {
        return Stream.iterate(
                () -> nextRow(rs)
                        ? Option.of(getRowValues(rs, request))
                        : Option.none())
            .collect(Vector.collector());
    }

    boolean nextRow(ResultSet rs) {
        try {
            return rs.next();
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    Map<String, Object> getRowValues(ResultSet rs, SelectParams request) {
        return Stream.ofAll(request.columns)
            .zipWithIndex()
            .map((z) -> Tuple.of(z._1, getColumnValue(rs, z._2)))
            .collect(LinkedHashMap.collector());
    }

    Object getColumnValue(ResultSet rs, int pos) {
        try {
            return rs.getObject(pos);
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

}
