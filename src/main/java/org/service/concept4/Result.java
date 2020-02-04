package org.service.concept4;

import io.vavr.collection.List;

public class Result {

    public static Result     EMPTY = new Result(List.empty(), List.empty(), List.empty());

    public final List<Event> events;
    public final List<Patch> patches;
    public final List<Error> errors;

    public Result(final List<Event> events, List<Patch> patches, List<Error> errors) {
        this.events = events;
        this.patches = patches;
        this.errors = errors;
    }
}
