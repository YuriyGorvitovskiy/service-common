package org.service.common.http.server;

import org.service.common.http.server.undertow.Undertow;

public interface HttpServer {

    public static HttpServer create(HttpServerConfig config) {
        if (Undertow.ENGINE.equals(config.engine)) {
            return new Undertow(config);
        }
        throw new RuntimeException("Unsupported HTTP Server engine: " + config.engine);
    }

    public void start();
}
