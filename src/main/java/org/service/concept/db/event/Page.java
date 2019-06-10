package org.service.concept.db.event;

public class Page {
    public final long start;
    public final long length;

    public Page(long start, long length) {
        this.start = start;
        this.length = length;
    }
}
