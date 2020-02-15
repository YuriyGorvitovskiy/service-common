package org.service.dbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface DBStatementBatchBinder<T> {
    void prepare(PreparedStatement ps, T item) throws SQLException;
}
