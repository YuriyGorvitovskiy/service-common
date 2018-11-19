package org.service.common.http.server;

import org.service.common.http.server.undertow.Undertow;

public class HttpServerConfig {
    public String engine  = Undertow.ENGINE;
    public String address = "0.0.0.0";
    public int    port    = 8080;
}
