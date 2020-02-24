package org.service.database.postgres.action.model;

import org.service.action2.Service;
import org.service.dbc.DBConnection;

public class Context {

    @Service("database")
    public final DBConnection dbc;

    public Context(DBConnection dbc) {
        this.dbc = dbc;
    }

}
