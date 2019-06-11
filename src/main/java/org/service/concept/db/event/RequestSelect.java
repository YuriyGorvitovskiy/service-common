package org.service.concept.db.event;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class RequestSelect {
    public final String                 table;
    public final ImmutableList<String>  columns;
    public final Condition              condition;
    public final ImmutableList<Sorting> sortings;
    public final Page                   page;

    public RequestSelect(String table,
                         List<String> columns,
                         Condition condition,
                         List<Sorting> sortings,
                         Page page) {
        this.table = table;
        this.columns = ImmutableList.copyOf(columns);
        this.condition = condition;
        this.sortings = ImmutableList.copyOf(sortings);
        this.page = page;
    }
}
