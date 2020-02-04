package org.service.concept4.persistence;

import java.sql.Connection;

import org.service.concept4.IContext;
import org.service.concept4.ILambda;
import org.service.concept4.IParams;
import org.service.concept4.Result;

public class CreateTable implements ILambda<CreateTable.Params, CreateTable.Context> {

    public static class Params implements IParams {
        final Table table;

        public Params(Table table) {
            this.table = table;
        }
    }

    public static class Context implements IContext {

        final Connection dbc;

        public Context(Connection dbc) {
            this.dbc = dbc;
        }
    }

    @Override
    public Result execute(Params params, Context context) {

        return Result.EMPTY;
    }
}
