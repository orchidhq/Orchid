package com.eden.orchid.server.server;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

@Singleton
public class StaticServer implements HttpHandler {

    private OrchidContext context;
    private int port;
    private HttpServer server;
    private File rootFolder;

    private RenderIndex renderIndex;
    private RenderFile renderFile;
    private Render404 render404;

    private String[] indexFiles = new String[] { "index.html", "index.htm" };

    @Inject
    public StaticServer(OrchidContext context, RenderIndex renderIndex, RenderFile renderFile, Render404 render404) {
        this.context = context;
        this.renderIndex = renderIndex;
        this.renderFile = renderFile;
        this.render404 = render404;
    }

    public void start(int port) {
        this.port = getFreePorts(port, Integer.min(port + 1000, 65535));
        String baseDir = context.query("options.d").toString();
        this.rootFolder = new File(baseDir);

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

        if(socket != null) {
            return socket.getLocalPort();
        }
        else {
            throw new IllegalStateException(Clog.format("Could not find a free port to allocate within range #{$1}-#{$2}.", rangeMin, rangeMax));
        }
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        String targetPath = t.getRequestURI().getPath();

        // Check if file exists
        File targetFile = new File(rootFolder, targetPath.replace('/', File.separatorChar));

        if (targetFile.exists()) {
            if (targetFile.isDirectory()) {

                boolean rendered = false;
                for(String indexFile : indexFiles) {
                    String indexPath = StringUtils.strip(targetPath, "/") + "/" + indexFile;

                    File targetIndexFile = new File(rootFolder, indexPath.replace('/', File.separatorChar));

                    if (targetIndexFile.exists()) {
                        renderFile.render(t, targetIndexFile, targetPath);
                        rendered = true;
                        break;
                    }
                }

                if(!rendered) {
                    renderIndex.render(t, targetFile, targetPath);
                }
            }
            else {
                renderFile.render(t, targetFile, targetPath);
            }
        }
        else {
            render404.render(t, targetFile, targetPath);
        }
    }
}