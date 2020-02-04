package org.service.concept4.persistence;

import java.sql.Connection;

import org.service.concept4.IContext;
import org.service.concept4.ILambda;
import org.service.concept4.IParams;
import org.service.concept4.Result;

public class Select implements ILambda<Select.Params, Select.Context> {

    public static class Params implements IParams {

        final Row row;

        public Params(Row row) {
            this.row = row;
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
