package org.service.concept.db.event;

public class ConditionLike implements Condition {
    public final String column;
    public final Object value;

    public ConditionLike(String column, Object value) {
        this.column = column;
        this.value = value;
    }

}
