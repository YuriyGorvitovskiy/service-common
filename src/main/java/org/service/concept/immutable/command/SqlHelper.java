package org.service.concept.immutable.command;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.MapUtils;

import com.google.common.collect.ImmutableMap;

public class SqlHelper {

    public static void appendWhere(StringBuilder sql, List<Object> params, Map<String, Object> filters) {
        if (MapUtils.isEmpty(filters)) {
            return;
        }

        sql.append(" WHERE ");
        boolean first = true;
        for (Entry<String, Object> filter : filters.entrySet()) {
            if (first) {
                first = false;
            } else {
                sql.append(" AND ");
            }
            sql.append(filter.getKey());
            sql.append(" = ? ");
            params.add(filter.getValue());
        }
    }

    public static void appendSet(StringBuilder sql, List<Object> params, ImmutableMap<String, Object> values) {
        if (MapUtils.isEmpty(values)) {
            return;
        }

        sql.append(" SET ");
        boolean first = true;
        for (Entry<String, Object> value : values.entrySet()) {
            if (first) {
                first = false;
            } else {
                sql.append(", ");
            }
            sql.append(value.getKey());
            sql.append(" = ? ");
            params.add(value.getValue());
        }
    }

    public static void setParams(PreparedStatement ps, List<Object> params) throws SQLException {
        int i = 0;
        for (Object param : params) {
            ps.setObject(++i, param);
        }
    }

    public static void appendValues(StringBuilder sql, List<Object> params, ImmutableMap<String, Object> values) {
        // TODO Auto-generated method stub

    }

}
