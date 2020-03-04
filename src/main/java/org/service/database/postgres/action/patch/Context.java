package org.service.database.postgres.action.patch;

import org.service.action2.Service;
import org.service.dbc.DBConnection;
import org.service.model.cache.TableInfo;

public class Context {

    @Service("database")
    public final DBConnection dbc;

    @Service("model")
    public final TableInfo tableInfo;

    public Context(DBConnection dbc, TableInfo tableInfo) {
        this.dbc = dbc;
        this.tableInfo = tableInfo;
    }

}
