package org.service.concept.db.event;

public class ConditionNull implements Condition {
    public final String column;

    public ConditionNull(String column) {
        this.column = column;
    }
}
