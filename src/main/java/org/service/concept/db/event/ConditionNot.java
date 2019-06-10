package org.service.concept.db.event;

public class ConditionNot implements Condition {
    public final Condition condition;

    public ConditionNot(Condition condition) {
        this.condition = condition;
    }
}
