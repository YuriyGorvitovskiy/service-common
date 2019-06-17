package org.service.command;

import io.vavr.collection.Seq;

public class Payload {
    public final Seq<Request<?>> requests;

    public Payload(Seq<Request<?>> requests) {
        this.requests = requests;
    }
}
