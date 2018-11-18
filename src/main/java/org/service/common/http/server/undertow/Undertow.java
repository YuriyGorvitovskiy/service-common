package org.service.common.http.server.undertow;

import org.service.common.http.server.HttpServer;
import org.service.common.http.server.HttpServerConfig;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class Undertow implements HttpServer {

    public static final String ENGINE = "UNDERTOW";

    HttpServerConfig config;

    public Undertow(HttpServerConfig config) {
        this.config = config;
    }

    @Override
    public void start() {
        io.undertow.Undertow.builder()
            .addHttpListener(config.port, config.address)
            .setHandler(new HttpHandler() {
                @Override
                public void handleRequest(final HttpServerExchange exchange) throws Exception {
                    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                    exchange.getResponseSender().send("Hello World");
                }
            }).build()
            .start();
    }
}
