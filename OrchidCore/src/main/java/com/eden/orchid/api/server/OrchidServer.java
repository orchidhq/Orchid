package com.eden.orchid.api.server;

import com.eden.orchid.api.OrchidContext;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Set;

@Getter @Setter
@Singleton
public class OrchidServer {

    private OrchidContext context;

    private int httpServerPort;
    private int websocketPort;

    private OrchidWebserver server;
    private OrchidWebsocket websocket;

    private Set<OrchidController> controllers;
    private OrchidFileController fileController;

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