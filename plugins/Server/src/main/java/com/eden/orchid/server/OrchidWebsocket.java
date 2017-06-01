package com.eden.orchid.server;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.server.api.ServerUtils;
import fi.iki.elonen.NanoWSD;
import lombok.Data;

import java.io.IOException;
import java.util.EventListener;

@Data
public class OrchidWebsocket extends NanoWSD implements EventListener {

    private OrchidContext context;
    private WebSocket webSocket;

    private int timeoutMinutes = 30;

    public OrchidWebsocket(OrchidContext context, int port) throws IOException {
        super(ServerUtils.getNearestFreePort(port));
        this.context = context;
        context.getInjector().injectMembers(this);

        start(timeoutMinutes * 60 * 1000, true);
        System.out.println("\nWebsocket running! Point your browsers to http://localhost:" + getListeningPort() + "/ \n");
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

    private static class WebsocketHandler extends WebSocket {
        public WebsocketHandler(IHTTPSession handshakeRequest) {
            super(handshakeRequest);
        }

        @Override
        protected void onOpen() {
            Clog.d("Opened");
        }

        @Override
        protected void onClose(WebSocketFrame.CloseCode code, String reason, boolean initiatedByRemote) {
            Clog.d("Closed [#{$1}] #{$2}#{$3}", new Object[]{
                    (initiatedByRemote ? "Remote" : "Self"),
                    (code != null ? code : "UnknownCloseCode[" + code + "]"),
                    (reason != null && !reason.isEmpty() ? ": " + reason : "")
            });
        }

        @Override
        protected void onMessage(WebSocketFrame message) {
            try {
                message.setUnmasked();
                sendFrame(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPong(WebSocketFrame pong) {
            Clog.d("Pong " + pong);
        }

        @Override
        protected void onException(IOException exception) {
            Clog.e("Exception", exception);
        }

        @Override
        protected void debugFrameReceived(WebSocketFrame frame) {
            Clog.d("Received " + frame);
        }

        @Override
        protected void debugFrameSent(WebSocketFrame frame) {
            Clog.d("Sent " + frame);
        }
    }
}
