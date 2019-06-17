package org.service.concept.db.event;

public class RequestCount {
    public final String    table;
    public final Condition condition;

    public RequestCount(String table,
                        Condition condition) {
        this.table = table;
        this.condition = condition;
    }
}
