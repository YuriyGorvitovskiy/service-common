package org.service.command;

public class Request<P> {

    public final String id;

    public final P      params;

    public Request(String id, P params) {
        this.id = id;
        this.params = params;
    }

}
