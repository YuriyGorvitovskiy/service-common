package org.service.command.dml;

import org.service.command.dml.predicate.Predicate;

public class DeleteParams implements DMLParams {
    public final String    table;
    public final Predicate filter;

    public DeleteParams(String table, Predicate filter) {
        this.table = table;
        this.filter = filter;
    }
}
