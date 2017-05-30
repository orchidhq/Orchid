package com.eden.orchid.server.server;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public interface RequestHandler {
    boolean canHandle(HttpExchange t, String targetPath);
    void render(HttpExchange t, String targetPath) throws IOException;
}
