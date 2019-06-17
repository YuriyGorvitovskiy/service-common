package org.service.concept.db.event;

public class ConditionMore implements Condition {
    public final String column;
    public final Object value;

    public ConditionMore(String column, Object value) {
        this.column = column;
        this.value = value;
    }

}
