package org.service.sql.simple;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.service.immutable.schema.DataType;

public abstract class Condition {

    public final String   column;
    public final DataType type;

    public Condition(String column, DataType type) {
        this.column = column;
        this.type = type;
    }

    public abstract String sql();

    public abstract int bind(PreparedStatement ps, int pos) throws SQLException;
}
