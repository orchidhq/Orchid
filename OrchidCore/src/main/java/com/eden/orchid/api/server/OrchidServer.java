package com.eden.orchid.api.server;

import com.eden.orchid.api.OrchidContext;
import lombok.Getter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Set;

@Singleton
public class OrchidServer {

    @Getter
    private final OrchidContext context;

    @Getter private int httpServerPort;
    @Getter private int websocketPort;

    @Getter public OrchidWebserver server;
    @Getter public OrchidWebsocket websocket;

    @Getter private final Set<OrchidController> controllers;
    @Getter private final OrchidFileController fileController;

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
}