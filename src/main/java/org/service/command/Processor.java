package org.service.command;

import javax.validation.constraints.NotNull;

import com.google.common.eventbus.EventBus;

import io.vavr.Tuple2;

/**
 * To
 * @author Yuriy Gorvitovskiy
 *
 */
public abstract class Processor {

    @NotNull
    protected final EventBus requestBus;

    @NotNull
    protected final EventBus responseBus;

    @NotNull
    protected final EventBus payloadBus;

    public Processor(@NotNull EventBus requestBus, @NotNull EventBus responseBus, @NotNull EventBus payloadBus) {
        this.requestBus = requestBus;
        this.responseBus = responseBus;
        this.payloadBus = payloadBus;
    }

    public void register() {
        this.requestBus.register(this);
    }

    public void unregister() {
        this.requestBus.unregister(this);
    }

    protected <P, R> void success(@NotNull Request<P> request, @NotNull Tuple2<R, Payload> result) {
        if (null != result._2) {
            request(result._2);
        }
        reply(new Response<>(request.id, result._1));
    }

    protected <P> void failure(@NotNull Request<P> request, @NotNull Throwable ex) {
        reply(new Failure(request.id, ex));
    }

    protected <P, R> void reply(@NotNull Request<P> request, @NotNull R result) {
        reply(new Response<>(request.id, result));
    }

    protected void request(@NotNull Payload payload) {
        payload.requests.forEach(r -> payloadBus.post(r));
    }

    protected void reply(@NotNull Response<?> response) {
        responseBus.post(response);
    }

}
