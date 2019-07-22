package com.eden.orchid.api.server;

import com.eden.orchid.api.OrchidContext;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Set;

@Singleton
public class OrchidServer {
    private final OrchidContext context;
    private int httpServerPort;
    private int websocketPort;
    public OrchidWebserver server;
    public OrchidWebsocket websocket;
    private final Set<OrchidController> controllers;
    private final OrchidFileController fileController;

    @Inject
    public OrchidServer(OrchidContext context, Set<OrchidController> controllers, OrchidFileController fileController) {
        this.context = context;
        this.controllers = controllers;
        this.fileController = fileController;
    }

    public void start(int port) throws IOException {
        // TODO: setup assistedInject for these
        this.server = new OrchidWebserver(context, controllers, fileController, port);
        this.httpServerPort = this.server.getListeningPort();
        this.websocket = new OrchidWebsocket(context, port);
        this.websocketPort = this.websocket.getListeningPort();
    }

    public OrchidContext getContext() {
        return this.context;
    }

    public int getHttpServerPort() {
        return this.httpServerPort;
    }

    public int getWebsocketPort() {
        return this.websocketPort;
    }

    public OrchidWebserver getServer() {
        return this.server;
    }

    public OrchidWebsocket getWebsocket() {
        return this.websocket;
    }

    public Set<OrchidController> getControllers() {
        return this.controllers;
    }

    public OrchidFileController getFileController() {
        return this.fileController;
    }
}
