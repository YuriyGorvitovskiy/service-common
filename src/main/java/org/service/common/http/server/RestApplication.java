package org.service.common.http.server;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

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

        // Endpoints
        for (Class<?> endPoint : endPoints) {
            s.add(endPoint);
        }

        return s;
    }

}
