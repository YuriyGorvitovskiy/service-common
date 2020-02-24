package org.service.action2.model.postgres;

import org.service.action2.Service;
import org.service.dbc.DBConnection;

public class Context {

    @Service("database.connection.postgres")
    public final DBConnection dbc;

    public Context(DBConnection dbc) {
        this.dbc = dbc;
    }

}
