package org.service.common.http.server;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.service.common.http.server.filter.CORSFilter;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;

public abstract class RestApplication extends Application {

    final Class<?>[] endPoints;

    protected RestApplication(Class<?>... endPoints) {
        this.endPoints = endPoints;
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<>();

        // Jackson JSON provider with C8 configuration
        s.add(JacksonJsonProvider.class);
        s.add(CORSFilter.class);

        // Endpoints
        for (Class<?> endPoint : endPoints) {
            s.add(endPoint);
        }

        // Swagger's Open API
        s.add(AcceptHeaderOpenApiResource.class);
        s.add(OpenApiResource.class);

        return s;
    }

}
