package com.eden.orchid.api.server;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import fi.iki.elonen.NanoWSD;

import java.io.IOException;

public final class OrchidWebsocket extends NanoWSD {

    private final OrchidContext context;
    private WebSocket webSocket;

    private int timeoutMinutes = 30;

    public OrchidWebsocket(OrchidContext context, int port) throws IOException {
        super(ServerUtils.getNearestFreePort(port));
        this.context = context;

        start(timeoutMinutes * 60 * 1000, true);
        Clog.i("Websocket running at http://localhost:" + getListeningPort() + "/");
    }

    @Override
    protected WebSocket openWebSocket(IHTTPSession handshake) {
        this.webSocket = new WebsocketHandler(handshake);
        return this.webSocket;
    }

    public void sendMessage(String message) {
        try {
            if(webSocket != null && webSocket.isOpen()) {
                webSocket.send(message);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class WebsocketHandler extends WebSocket {
        public WebsocketHandler(IHTTPSession handshakeRequest) {
            super(handshakeRequest);
        }

        @Override
        protected void onOpen() {
            Clog.d("Websocket Opened");
        }

        @Override
        protected void onClose(WebSocketFrame.CloseCode code, String reason, boolean initiatedByRemote) {
            Clog.d("Websocket Closed");
        }

        @Override
        protected void onMessage(WebSocketFrame message) {
            try {
                message.setUnmasked();

                if(message.getTextPayload().equalsIgnoreCase("exit")) {
                    context.broadcast(Orchid.Lifecycle.EndSession.fire(this));
                }
                else if(message.getTextPayload().equalsIgnoreCase("rebuild")) {
                    context.build();
                }

                sendFrame(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPong(WebSocketFrame pong) {
        }

        @Override
        protected void onException(IOException exception) {
            Clog.e("Exception", exception);
        }

        @Override
        protected void debugFrameReceived(WebSocketFrame frame) {
        }

        @Override
        protected void debugFrameSent(WebSocketFrame frame) {
        }
    }
}
