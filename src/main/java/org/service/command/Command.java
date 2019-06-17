package org.service.command;

@FunctionalInterface
public interface Command<P, C, R> {
    public R apply(P params, C context);
}
