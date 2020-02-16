package org.service.sql.simple;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.service.immutable.schema.DataType;

import io.vavr.collection.List;

public class In<T> extends Condition {

    public final List<T> values;

    public In(String column, DataType type, List<T> values) {
        super(column, type);
        this.values = values;
    }

    @Override
    public String sql() {
        return column + " IN " + StringUtils.repeat("?", ", ", values.size());
    }

    @Override
    public int bind(PreparedStatement ps, int pos) throws SQLException {
        for (T value : values) {
            type.set(ps, pos++, value);
        }
        return pos;
    }

}
