package org.service.action2;

@FunctionalInterface
public interface IResolver<P, C> {
    C resolve(P params);
}
