package org.service.concept.db.event;

public class ConditionLess implements Condition {
    public final String column;
    public final Object value;

    public ConditionLess(String column, Object value) {
        this.column = column;
        this.value = value;
    }

}
