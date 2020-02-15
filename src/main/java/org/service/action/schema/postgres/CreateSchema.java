package org.service.action.schema.postgres;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.service.action.Action;
import org.service.action.IAction;
import org.service.action.Result;

import io.vavr.collection.List;

@Action(service = "schema_manager", name = "create_schema")
public class CreateSchema implements IAction<CreateSchema.Params, Context> {

    public static class Params {
        public final String                      name;
        public final List<CreateTable.Params>    tables;
        public final List<CreateSequence.Params> sequences;

        Params(String name, List<CreateTable.Params> tables, List<CreateSequence.Params> sequences) {
            this.name = name;
            this.tables = tables;
            this.sequences = sequences;
        }
    }

    @Override
    public Result apply(Params params, Context ctx) {
        String ddl = "CREATE SCHEMA " + params.name;
        try (PreparedStatement ps = ctx.dbc.prepareStatement(ddl)) {
            ps.execute();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        CreateTable createTable = new CreateTable();
        for (CreateTable.Params table : params.tables) {
            createTable.apply(table, ctx);
        }

        CreateSequence createSequence = new CreateSequence();
        for (CreateSequence.Params sequence : params.sequences) {
            createSequence.apply(sequence, ctx);
        }

        return Result.empty;
    }
}
