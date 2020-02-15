package org.service.action.schema.postgres;

import org.service.dbc.DBConnection;

public class Context {

    public final DBConnection dbc;

    public Context(DBConnection dbc) {
        this.dbc = dbc;
    }

}
