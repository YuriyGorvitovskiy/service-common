package org.service.dbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface DBStatementBinder {

    static final DBStatementBinder EMPTY = ps -> {};

    void prepare(PreparedStatement ps) throws SQLException;
}
