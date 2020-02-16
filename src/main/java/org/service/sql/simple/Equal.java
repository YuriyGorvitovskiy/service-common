package org.service.sql.simple;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.service.immutable.schema.DataType;

public class Equal<T> extends Condition {

    public final T value;

    public Equal(String column, DataType type, T value) {
        super(column, type);
        this.value = value;
    }

    @Override
    public String sql() {
        return column + " = ?";
    }

    @Override
    public int bind(PreparedStatement ps, int pos) throws SQLException {
        type.set(ps, pos++, value);
        return pos;
    }

    public static <T> Equal<T> of(String column, DataType type, T value) {
        return new Equal<>(column, type, value);
    }
}
