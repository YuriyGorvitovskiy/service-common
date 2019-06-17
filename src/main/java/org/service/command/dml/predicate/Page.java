package org.service.concept.db.event;

public class Page {
    public final long offset;
    public final long limit;

    public Page(long offset, long limit) {
        this.offset = offset;
        this.limit = limit;
    }
}
