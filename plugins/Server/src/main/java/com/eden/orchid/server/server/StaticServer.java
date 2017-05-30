package com.eden.orchid.server.server;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.server.server.file.FileHandler;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Set;

@Singleton
public class StaticServer implements HttpHandler {

    private OrchidContext context;
    private int port;
    private HttpServer server;

    private FileHandler fileHandler;
    private Set<RequestHandler> requestHandlers;

    @Inject
    public StaticServer(
            OrchidContext context,
            FileHandler fileHandler,
            Set<RequestHandler> requestHandlers) {
        this.context = context;
        this.fileHandler = fileHandler;
        this.requestHandlers = requestHandlers;
    }

    public void start(int port) {
        this.port = getFreePorts(port, Integer.min(port + 1000, 65535));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            context.broadcast(Orchid.Events.SHUTDOWN, false);
        }));

        new Thread(() -> {
            Clog.i("Starting http server on port #{$1}", StaticServer.this.port);
            try {
                server = HttpServer.create(new InetSocketAddress(StaticServer.this.port), 0);
                server.createContext("/", this);
                server.setExecutor(null);
                server.start();
            }
            catch (Exception e) {
                Clog.i("Initialization failed", e);
            }
            Clog.i("Started HttpServer on port #{$1}", StaticServer.this.port);
        }).start();
    }

    public void stop() {
        server.stop(0);
        server = null;
        Clog.i("Stopped Server running on port #{$1}", port);
    }

    public int getPort() {
        return port;
    }

    private int getFreePorts(int rangeMin, int rangeMax) {
        ServerSocket socket = null;
        for (int i = rangeMin; i <= rangeMax; i++) {
            try {
                socket = new ServerSocket(i);
                socket.close();
                break;
            }
            catch (IOException e) {
            }
        }

        if (socket != null) {
            return socket.getLocalPort();
        }
        else {
            throw new IllegalStateException(Clog.format("Could not find a free port to allocate within range #{$1}-#{$2}.", rangeMin, rangeMax));
        }
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        String targetPath = t.getRequestURI().getPath();

        String queryParamString = t.getRequestURI().getQuery();

        boolean forceFileOverride = false;
        boolean handled = false;

        if(!EdenUtils.isEmpty(queryParamString)) {
            forceFileOverride = queryParamString.contains("forcefileoverride=true");
        }

        if(!forceFileOverride) {
            for (RequestHandler handler : requestHandlers) {
                if(handler.canHandle(t, targetPath)) {
                    handler.render(t, targetPath);
                    handled = true;
                    break;
                }
            }
        }

        if(!handled) {
            fileHandler.render(t, targetPath);
        }
    }





    public static void renderString(HttpExchange t, String content) throws IOException {
        renderString(t, content, "text/html");
    }

    public static void renderString(HttpExchange t, String content, String contentType) throws IOException {
        Headers responseHeaders = t.getResponseHeaders();
        responseHeaders.set("Content-Type", contentType + "; charset=UTF-8");
        t.sendResponseHeaders(200, content.length());
        IOUtils.write(content, t.getResponseBody());
        t.getResponseBody().close();
    }

    public static void renderJSON(HttpExchange t, JSONObject data) throws IOException {
        renderString(t, data.toString(), "application/json");
    }

    public static void renderJSON(HttpExchange t, JSONArray data) throws IOException {
        renderString(t, data.toString(), "application/json");
    }
}