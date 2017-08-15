package com.eden.orchid.server;

import com.eden.orchid.api.OrchidContext;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Getter @Setter
@Singleton
public class OrchidServer {

    private OrchidContext context;
    private int httpServerPort;
    private OrchidWebserver server;

    private int websocketPort;
    private OrchidWebsocket websocket;

    @Inject
    public OrchidServer(OrchidContext context) {
        this.context = context;
    }

    public void start(int port) throws IOException {
        this.server = new OrchidWebserver(context, port);
        this.httpServerPort = this.server.getListeningPort();

        this.websocket = new OrchidWebsocket(context, port);
        this.websocketPort = this.websocket.getListeningPort();
    }
}