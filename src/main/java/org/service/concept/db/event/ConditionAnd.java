package org.service.concept.db.event;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class ConditionAnd implements Condition {
    public final ImmutableList<Condition> conditions;

    public ConditionAnd(List<Condition> conditions) {
        this.conditions = ImmutableList.copyOf(conditions);
    }
}
