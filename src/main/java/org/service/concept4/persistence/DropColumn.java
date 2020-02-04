package org.service.concept4.persistence;

import java.sql.Connection;

import org.service.concept4.IContext;
import org.service.concept4.ILambda;
import org.service.concept4.IParams;
import org.service.concept4.Result;

public class DropColumn implements ILambda<DropColumn.Params, DropColumn.Context> {

    public static class Params implements IParams {

        final String table;

        final String column;

        public Params(String table, String column) {
            this.table = table;
            this.column = column;
        }
    }

    public static class Context implements IContext {

        final Connection dbc;

        final Table      table;

        final Column     column;

        public Context(Connection dbc, Table table, Column column) {
            this.dbc = dbc;
            this.table = table;
            this.column = column;
        }
    }

    @Override
    public Result execute(Params params, Context context) {

        return Result.EMPTY;
    }
}
