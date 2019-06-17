package org.service.command.dml;

import org.service.command.dml.predicate.Predicate;

import io.vavr.collection.Map;

public class UpdateParams implements DMLParams {
    public final String              table;
    public final Map<String, Object> values;
    public final Predicate           filter;

    public UpdateParams(String table, Map<String, Object> values, Predicate filter) {
        this.table = table;
        this.values = values;
        this.filter = filter;
    }
}
