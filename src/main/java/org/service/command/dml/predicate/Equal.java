package org.service.concept.db.event;

public class ConditionEqual implements Condition {
    public final String column;
    public final Object value;

    public ConditionEqual(String column, Object value) {
        this.column = column;
        this.value = value;
    }
}
