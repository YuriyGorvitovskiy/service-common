package org.service.concept4;

@FunctionalInterface
public interface ILambda<P extends IParams, C extends IContext> {

    Result execute(P params, C context);

}
