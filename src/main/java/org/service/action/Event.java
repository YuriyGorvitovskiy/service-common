package org.service.action;

public class Event<P> {
    public final String service;
    public final String action;
    public final P      params;

    public Event(String service, String action, P params) {
        this.service = service;
        this.action = action;
        this.params = params;
    }

}
