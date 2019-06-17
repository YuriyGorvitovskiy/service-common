package org.service.command;

public class Failure extends Response<Throwable> {

    public Failure(String request_id, Throwable result) {
        super(request_id, result);
    }
}
