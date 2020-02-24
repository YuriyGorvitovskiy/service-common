package org.service.database.postgres.action.patch;

import org.service.action2.Service;
import org.service.dbc.DBConnection;
import org.service.immutable.schema.DataType;

import io.vavr.collection.Map;
import io.vavr.collection.Set;

public class Context {

    @Service("database")
    public final DBConnection dbc;

    @Service("model")
    public final Map<String, DataType> columnsType;

    @Service("model")
    public final Set<String> primaryColumns;

    public Context(DBConnection dbc, Map<String, DataType> columnsType, Set<String> primaryColumns) {
        this.dbc = dbc;
        this.columnsType = columnsType;
        this.primaryColumns = primaryColumns;
    }

}
