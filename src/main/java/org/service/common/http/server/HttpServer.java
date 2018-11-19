package org.service.common.http.server;

import javax.ws.rs.core.Application;

import org.service.common.http.server.undertow.Undertow;

public interface HttpServer {

    public static HttpServer create(HttpServerConfig config, Class<? extends Application> restAppClass) {
        if (Undertow.ENGINE.equals(config.engine)) {
            return new Undertow(config, restAppClass);
        }
        throw new RuntimeException("Unsupported HTTP Server engine: " + config.engine);
    }

    public void start();
}
