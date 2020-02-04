package org.service.concept4.persistence;

import java.sql.Connection;

import org.service.concept4.IContext;
import org.service.concept4.ILambda;
import org.service.concept4.IParams;
import org.service.concept4.Result;

public class CreateColumn implements ILambda<CreateColumn.Params, CreateColumn.Context> {

    public static class Params implements IParams {
        final String table;
        final Column column;

        public Params(String table, Column column) {
            this.table = table;
            this.column = column;
        }
    }

    public static class Context implements IContext {

        final Connection dbc;
        final Table      table;

        public Context(Connection dbc, Table table) {
            this.dbc = dbc;
            this.table = table;
        }
    }

    @Override
    public Result execute(Params params, Context context) {

        return Result.EMPTY;
    }
}
