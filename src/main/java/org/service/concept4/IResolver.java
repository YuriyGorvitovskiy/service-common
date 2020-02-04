package org.service.concept4;

@FunctionalInterface
public interface IResolver<P extends IParams, C extends IContext> {

    C resolve(P params);

}
