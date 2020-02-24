package org.service.action2;

public class Event<P> {
    public final String action;
    public final P      params;

    public Event(String action, P params) {
        this.action = action;
        this.params = params;
    }
}
