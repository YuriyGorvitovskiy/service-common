package org.service.concept.db.event;

public class RequestDelete {
    public final String    table;
    public final Condition condition;

    public RequestDelete(String table, Condition condition) {
        this.table = table;
        this.condition = condition;
    }
}
