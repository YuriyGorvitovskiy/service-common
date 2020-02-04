package org.service.concept4;

public class Handler<P extends IParams, C extends IContext> {

    public final IResolver<P, C> resolver;
    public final ILambda<P, C>   lambda;

    public Handler(IResolver<P, C> resolver, ILambda<P, C> lambda) {
        this.resolver = resolver;
        this.lambda = lambda;
    }
}
