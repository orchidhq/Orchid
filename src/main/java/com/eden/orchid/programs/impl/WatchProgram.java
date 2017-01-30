package com.eden.orchid.programs.impl;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.Orchid;
import com.eden.orchid.programs.Program;
import com.eden.orchid.utilities.AutoRegister;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.nio.file.LinkOption.*;
import static java.nio.file.StandardWatchEventKinds.*;

@AutoRegister
public class WatchProgram implements Program {

    private static WatchService watcher;
    private static Map<WatchKey, Path> keys;

    @Override
    public String getName() {
        return "watch";
    }

    @Override
    public String getDescription() {
        return "Starts a simple HTTP server and watches your resource directory for changes.";
    }

    @Override
    public void run() {
        if (Orchid.query("options.resourcesDir") != null) {
            String rootDir = Orchid.query("options.resourcesDir").toString();

            File file = new File(rootDir);

            if (file.exists() && file.isDirectory()) {
                try {
                    Clog.i("Watching root resources directory for changes");

                    JSONObject rootJson = Orchid.getRoot();
                    JSONObject optionsJson = rootJson.getJSONObject("options");

                    WatchProgram.StandaloneHttpServer server = new WatchProgram.StandaloneHttpServer(8080);
                    server.start();

                    optionsJson.put("baseUrl", "http://localhost:" + server.getPort());

                    rebuild();

                    Path root = Paths.get(rootDir);
                    watcher = FileSystems.getDefault().newWatchService();
                    keys = new HashMap<>();
                    registerAll(root);
                    processEvents();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                Clog.w("Resources directory doesn't exist or isn't is a directory");
            }
        }
        else {
            Clog.w("There is no resources directory to watch");
        }
    }

    // watch for changes and rebuild
//----------------------------------------------------------------------------------------------------------------------
    private void rebuild() {
        Orchid.build();
    }

    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        Path prev = keys.get(key);

        keys.put(key, dir);
    }

    private void registerAll(final Path start) throws IOException {
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private void processEvents() {
        while (true) {

            WatchKey key;
            try {
                key = watcher.take();
            }
            catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                if (kind == OVERFLOW) {
                    continue;
                }

                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path name = ev.context();
                Path child = dir.resolve(name);

                rebuild();

                if (kind == ENTRY_CREATE) {
                    try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }

    private static class StandaloneHttpServer implements HttpHandler {
        private int port = 0;
        private String baseUrl;
        private HttpServer server;
        private final File rootFolder;

        public StandaloneHttpServer(int port) {
            this.port = getFreePorts(port, Integer.min(port + 1000, 65535));
            String baseDir = Orchid.query("options.d").toString();
            this.baseUrl = "http://localhost:" + this.port;
            this.rootFolder = new File(baseDir);

            Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
        }

        public void start() {
            new Thread(() -> {
                Clog.i("Starting http server. (" + port + ")");
                try {
                    server = HttpServer.create(new InetSocketAddress(port), 0);
                    server.createContext("/", this);
                    server.setExecutor(null);
                    server.start();
                }
                catch (Exception e) {
                    Clog.i("Initialization failed." + port, e);
                }
                Clog.i("Started HttpServer:" + port);
                System.out.println("Started HttpServer:" + port);
            }).start();
        }

        public void stop() {
            server.stop(0);
            server = null;
            Clog.i("Stopped Server:" + port);
            System.out.println("Stopped Server:" + port);
        }

        public int getPort() {
            return port;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            String targetPath = t.getRequestURI().getPath();

            // Check if file exists
            File targetFile = new File(rootFolder, targetPath.replace('/', File.separatorChar));

            if (targetFile.exists()) {
                if (targetFile.isDirectory()) {
                    String indexPath = StringUtils.strip(targetPath, "/") + "/index.html";

                    File targetIndexFile = new File(rootFolder, indexPath.replace('/', File.separatorChar));

                    if (targetIndexFile.exists()) {
                        renderFile(t, targetIndexFile, targetPath);
                    }
                    else {
                        renderIndexPage(t, targetFile, targetPath);
                    }
                }
                else {
                    renderFile(t, targetFile, targetPath);
                }
            }
            else {
                // If it doesn't exist, send error
                String message = "404 Not Found " + targetFile.getAbsolutePath();
                t.sendResponseHeaders(404, 0);
                OutputStream out = t.getResponseBody();
                out.write(message.getBytes());
                out.close();
            }
        }

        private void renderFile(HttpExchange t, File targetFile, String targetPath) throws IOException {
            // If it exists and it's a file, serve it
            int bufLen = 10000 * 1024;
            byte[] buf = new byte[bufLen];
            int len = 0;
            Headers responseHeaders = t.getResponseHeaders();

            String mimeType;

            if (targetFile.isDirectory() || targetFile.getName().endsWith(".html") || targetFile.getName().endsWith(".htm")) {
                mimeType = "text/html; charset=UTF-8";
            }
            else if (targetFile.getName().endsWith(".css")) {
                mimeType = "text/css; charset=UTF-8";
            }
            else if (targetFile.getName().endsWith(".js")) {
                mimeType = "application/javascript; charset=UTF-8";
            }
            else if (targetFile.getName().endsWith(".json")) {
                mimeType = "application/json; charset=UTF-8";
            }

            else {
                mimeType = "text/plain; charset=UTF-8";
            }

            responseHeaders.set("Content-Type", mimeType);
            Clog.i("Server Directory Listing:" + targetPath);

            if (targetFile.isFile()) {
                t.sendResponseHeaders(200, targetFile.length());
                FileInputStream fileIn = new FileInputStream(targetFile);
                OutputStream out = t.getResponseBody();

                while ((len = fileIn.read(buf, 0, bufLen)) != -1) {
                    out.write(buf, 0, len);
                }

                out.close();
                fileIn.close();
            }
            else if (targetFile.isDirectory()) {
                renderIndexPage(t, targetFile, targetPath);
            }
        }

        private void renderIndexPage(HttpExchange t, File targetFile, String targetPath) throws IOException {
            Headers responseHeaders = t.getResponseHeaders();

            responseHeaders.set("Content-Type", "text/html; charset=UTF-8");
            Clog.i("Server Directory Listing:" + targetFile.getAbsolutePath());

            if (targetFile.isDirectory()) {
                File files[] = targetFile.listFiles();
                StringBuffer sb = new StringBuffer();
                sb.append("\n<html>");
                sb.append("\n<head>");
                sb.append("\n<style>");
                sb.append("\n.datagrid table { border-collapse: collapse; text-align: left; width: 100%; } .datagrid {font: normal 12px/150% Arial, Helvetica, sans-serif; background: #fff; overflow: hidden; border: 1px solid #006699; -webkit-border-radius: 3px; -moz-border-radius: 3px; border-radius: 3px; }.datagrid table td, .datagrid table th { padding: 3px 10px; }.datagrid table thead th {background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #006699), color-stop(1, #00557F) );background:-moz-linear-gradient( center top, #006699 5%, #00557F 100% );filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#006699', endColorstr='#00557F');background-color:#006699; color:#ffffff; font-size: 15px; font-weight: bold; border-left: 1px solid #0070A8; } .datagrid table thead th:first-child { border: none; }.datagrid table tbody td { color: #00496B; border-left: 1px solid #E1EEF4;font-size: 12px;font-weight: normal; }.datagrid table tbody .alt td { background: #E1EEF4; color: #00496B; }.datagrid table tbody td:first-child { border-left: none; }.datagrid table tbody tr:last-child td { border-bottom: none; }.datagrid table tfoot td div { border-top: 1px solid #006699;background: #E1EEF4;} .datagrid table tfoot td { padding: 0; font-size: 12px } .datagrid table tfoot td div{ padding: 2px; }.datagrid table tfoot td ul { margin: 0; padding:0; list-style: none; text-align: right; }.datagrid table tfoot  li { display: inline; }.datagrid table tfoot li a { text-decoration: none; display: inline-block;  padding: 2px 8px; margin: 1px;color: #FFFFFF;border: 1px solid #006699;-webkit-border-radius: 3px; -moz-border-radius: 3px; border-radius: 3px; background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #006699), color-stop(1, #00557F) );background:-moz-linear-gradient( center top, #006699 5%, #00557F 100% );filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#006699', endColorstr='#00557F');background-color:#006699; }.datagrid table tfoot ul.active, .datagrid table tfoot ul a:hover { text-decoration: none;border-color: #006699; color: #FFFFFF; background: none; background-color:#00557F;}div.dhtmlx_window_active, div.dhx_modal_cover_dv { position: fixed !important; }");
                sb.append("\n</style>");
                sb.append("\n<title>List of files/dirs under /scratch/mseelam/view_storage/mseelam_otd1/otd_test/./work</title>");
                sb.append("\n</head>");
                sb.append("\n<body>");
                sb.append("\n<div class=\"datagrid\">");
                sb.append("\n<table>");
                sb.append("\n<caption>Directory Listing</caption>");
                sb.append("\n<thead>");
                sb.append("\n	<tr>");
                sb.append("\n		<th>File</th>");
                sb.append("\n		<th>Dir ?</th>");
                sb.append("\n		<th>Size</th>");
                sb.append("\n		<th>Date</th>");
                sb.append("\n	</tr>");
                sb.append("\n</thead>");
                sb.append("\n<tfoot>");
                sb.append("\n	<tr>");
                sb.append("\n		<th>File</th>");
                sb.append("\n		<th>Dir ?</th>");
                sb.append("\n		<th>Size</th>");
                sb.append("\n		<th>Date</th>");
                sb.append("\n	</tr>");
                sb.append("\n</tfoot>");
                sb.append("\n<tbody>");

                int numberOfFiles = files.length;

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd-hh:mm:ss z", Locale.getDefault());

                for (int i = 0; i < numberOfFiles; i++) {
                    if (i % 2 == 0) { sb.append("\n\t<tr class='alt'>"); }
                    else { sb.append("\n\t<tr>"); }
                    if (files[i].isDirectory()) {
                        sb.append("\n\t\t<td><a href='" + this.baseUrl + "/" + StringUtils.strip(targetPath, "/") + "/" + files[i].getName() + "/'>" + files[i].getName() + "</a></td>" +
                                "<td>Y</td>" + "<td>" + files[i].length() +
                                "</td>" + "<td>" + formatter.format(new Date(files[i].lastModified())) + "</td>\n\t</tr>");
                    }
                    else {
                        sb.append("\n\t\t<td><a href='" + this.baseUrl + "/" + StringUtils.strip(targetPath, "/") + "/" + files[i].getName() + "'>" + files[i].getName() + "</a></td>" +
                                "<td> </td>" + "<td>" + files[i].length() +
                                "</td>" + "<td>" + formatter.format(new Date(files[i].lastModified())) + "</td>\n\t</tr>");
                    }
                }
                sb.append("\n</tbody>");
                sb.append("\n</table>");
                sb.append("\n</div>");
                sb.append("\n</body>");
                sb.append("\n</html>");

                t.sendResponseHeaders(200, sb.length());
                OutputStream out = t.getResponseBody();
                out.write(sb.toString().getBytes());
                out.close();
            }
        }

        private static boolean isPortFree(int port) {
            ServerSocket socket = null;
            try {
                socket = new ServerSocket(port);
                socket.close();
            }
            catch (IOException e) {
                return false;
            }
            return true;
        }

        private static int getFreePorts(int rangeMin, int rangeMax) {
            for (int i = rangeMin; i <= rangeMax; i++) {
                if (isPortFree(i)) {
                    return i;
                }
            }

            throw new IllegalStateException(Clog.format("Could not find a free port to allocate within range #{$1}-#{$2}.", rangeMin, rangeMax));
        }
    }

}

