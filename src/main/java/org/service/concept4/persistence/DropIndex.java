package org.service.concept4.persistence;

import java.sql.Connection;

import org.service.concept4.IContext;
import org.service.concept4.ILambda;
import org.service.concept4.IParams;
import org.service.concept4.Result;

public class DropIndex implements ILambda<DropIndex.Params, DropIndex.Context> {

    public static class Params implements IParams {

        final String table;

        final String index;

        public Params(String table, String index) {
            this.table = table;
            this.index = index;
        }
    }

    public static class Context implements IContext {

        final Connection dbc;

        final Table      table;

        final Index      index;

        public Context(Connection dbc, Table table, Index index) {
            this.dbc = dbc;
            this.table = table;
            this.index = index;
        }
    }

    @Override
    public Result execute(Params params, Context context) {

        return Result.EMPTY;
    }
}
