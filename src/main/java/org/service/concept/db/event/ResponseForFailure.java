package org.service.concept.db.event;

public class ResponseForFailure implements Response {
    public final Request request;
    public final String  message;
    public final String  exception;

    public ResponseForFailure(Request request, String message, String exception) {
        this.request = request;
        this.message = message;
        this.exception = exception;
    }
}
