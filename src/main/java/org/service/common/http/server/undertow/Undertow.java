package org.service.common.http.server.undertow;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.service.common.http.server.HttpServer;
import org.service.common.http.server.HttpServerConfig;

import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.util.Headers;

public class Undertow implements HttpServer {

    public static final String ENGINE = "UNDERTOW";

    HttpServerConfig             config;
    Class<? extends Application> restAppClass;

    public Undertow(HttpServerConfig config,
                    Class<? extends Application> restAppClass) {
        this.config = config;
        this.restAppClass = restAppClass;
    }

    @Override
    public void start() {
        io.undertow.Undertow.builder()
            .addHttpListener(config.port, config.address)
            .setHandler(createPathHandler())
            .build()
            .start();
    }

    HttpHandler createPathHandler() {
        return Handlers.path()
            .addPrefixPath("/", createHelloWorldHandler())
            .addPrefixPath(getRestPath(), createJerseyHandler());
    }

    HttpHandler createHelloWorldHandler() {
        return new HttpHandler() {
            @Override
            public void handleRequest(final HttpServerExchange exchange) throws Exception {
                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                exchange.getResponseSender().send("Hello World");
            }
        };
    }

    HttpHandler createJerseyHandler() {
        try {
            DeploymentManager manager = Servlets.defaultContainer()
                .addDeployment(Servlets.deployment()
                    .setClassLoader(Undertow.class.getClassLoader())
                    .setContextPath(getRestPath())
                    .setDeploymentName("RestAPI")
                    .addServlet(Servlets
                        .servlet("JerseyServlet", ServletContainer.class)
                        .setLoadOnStartup(1)
                        .addInitParam(ServletProperties.JAXRS_APPLICATION_CLASS, restAppClass.getName())
                        .addMapping("/*")));

            manager.deploy();

            return manager.start();
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    String getRestPath() {
        return restAppClass.getAnnotation(ApplicationPath.class).value();
    }
}
