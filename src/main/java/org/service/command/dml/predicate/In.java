package org.service.concept.db.event;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class ConditionIn implements Condition {
    public final String                column;
    public final ImmutableList<Object> values;

    public ConditionIn(String column, List<?> values) {
        this.column = column;
        this.values = ImmutableList.copyOf(values);
    }

}
