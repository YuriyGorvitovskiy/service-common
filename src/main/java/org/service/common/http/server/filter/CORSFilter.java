package org.service.common.http.server.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class CORSFilter implements ContainerResponseFilter {

    static interface Header {

        final static String ALLOW_ORIGIN      = "Access-Control-Allow-Origin";

        final static String ALLOW_HEADERS     = "Access-Control-Allow-Headers";

        final static String ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";

        final static String ALLOW_METHODS     = "Access-Control-Allow-Methods";

        final static String MAX_AGE           = "Access-Control-Max-Age";
    }

    static interface Values {

        final static String ALLOW_ORIGIN      = "*";

        final static String ALLOW_HEADERS     = "origin, content-type, accept, authorization";

        final static String ALLOW_CREDENTIALS = "true";

        final static String ALLOW_METHODS     = "GET, POST, PUT, DELETE, OPTIONS, HEAD";

        final static String MAX_AGE           = "1209600";
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        responseContext.getHeaders().add(Header.ALLOW_ORIGIN, Values.ALLOW_ORIGIN);
        responseContext.getHeaders().add(Header.ALLOW_HEADERS, Values.ALLOW_HEADERS);
        responseContext.getHeaders().add(Header.ALLOW_CREDENTIALS, Values.ALLOW_CREDENTIALS);
        responseContext.getHeaders().add(Header.ALLOW_METHODS, Values.ALLOW_METHODS);
        responseContext.getHeaders().add(Header.MAX_AGE, Values.MAX_AGE);
    }

}
