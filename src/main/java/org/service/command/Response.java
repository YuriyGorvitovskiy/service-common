package org.service.command;

public class Response<R> {
    public final String request_id;
    public final R      result;

    public Response(String request_id, R result) {
        this.request_id = request_id;
        this.result = result;
    }
}
