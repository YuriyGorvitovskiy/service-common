package org.service.dbc;

import java.sql.Connection;

@FunctionalInterface
public interface DBConnectionConsumer {

    void accept(Connection jdbc) throws Exception;
}
