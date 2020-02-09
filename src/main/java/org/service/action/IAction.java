package org.service.action;

public interface IAction<P, C> {

    Result apply(P params, C ctx);

}
