package org.service.action.schema.postgres;

import java.sql.Connection;

public class Context {

    public final Connection dbc;

    public Context(Connection dbc) {
        this.dbc = dbc;
    }

}
