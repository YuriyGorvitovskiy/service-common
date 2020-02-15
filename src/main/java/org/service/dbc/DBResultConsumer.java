package org.service.dbc;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface DBResultConsumer<T> {

    T accept(ResultSet rs) throws SQLException;
}
