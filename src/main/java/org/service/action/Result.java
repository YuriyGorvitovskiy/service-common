package org.service.action;

import org.service.immutable.data.Patch;

import io.vavr.collection.List;

public class Result {
    public static final Result  empty = Result.of(null, null);
    public final List<Patch>    patches;
    public final List<Event<?>> events;

    Result(List<Patch> patches, List<Event<?>> events) {
        this.patches = patches;
        this.events = events;
    }

    public static Result of(Patch patch) {
        return new Result(List.of(patch), List.empty());
    }

    public static Result of(Patch... patches) {
        return new Result(List.of(patches), List.empty());
    }

    public static Result of(Event<?> event) {
        return new Result(List.empty(), List.of(event));
    }

    public static Result of(Event<?>... events) {
        return new Result(List.empty(), List.of(events));
    }

    public static Result of(List<Patch> patches, List<Event<?>> events) {
        return new Result(
                null == patches ? List.empty() : patches,
                null == events ? List.empty() : events);
    }

    @SafeVarargs
    public static Result ofPatches(List<Patch>... patches) {
        return new Result(List.of(patches).flatMap(p -> p), List.empty());
    }

    @SafeVarargs
    public static Result ofEvents(List<Event<?>>... events) {
        return new Result(List.empty(), List.of(events).flatMap(e -> e));
    }

}
