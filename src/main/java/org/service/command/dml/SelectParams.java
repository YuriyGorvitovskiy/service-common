package org.service.command.dml;

import org.service.command.dml.predicate.OrderBy;
import org.service.command.dml.predicate.Page;
import org.service.command.dml.predicate.Predicate;

import io.vavr.collection.Seq;

public class SelectParams implements DMLParams {
    public final String       table;
    public final Seq<String>  columns;
    public final Predicate    filter;
    public final Seq<OrderBy> orderBy;
    public final Page         page;

    public SelectParams(String table,
                        Seq<String> columns,
                        Predicate filter,
                        Seq<OrderBy> sortings,
                        Page page) {
        this.table = table;
        this.columns = columns;
        this.filter = filter;
        this.orderBy = sortings;
        this.page = page;
    }
}
