package org.service.concept.db.event;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class RequestSelect {
    public final String                table;
    public final Condition             condition;
    public final ImmutableList<String> columns;
    public final ImmutableList<String> sorting;
    public final Page                  page;

    public RequestSelect(String table,
                         Condition condition,
                         List<String> columns,
                         List<String> sorting,
                         Page page) {
        this.table = table;
        this.condition = condition;
        this.columns = ImmutableList.copyOf(columns);
        this.sorting = ImmutableList.copyOf(sorting);
        this.page = page;
    }
}
