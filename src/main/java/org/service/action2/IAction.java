package org.service.action2;

import io.vavr.collection.List;

@FunctionalInterface
public interface IAction<P, C> {

    List<Event<?>> apply(P params, C context);
}
