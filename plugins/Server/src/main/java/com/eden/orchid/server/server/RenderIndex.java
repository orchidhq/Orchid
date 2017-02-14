package com.eden.orchid.server.server;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.OrchidResource;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.utilities.OrchidUtils;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Singleton
public class RenderIndex {

    private OrchidContext context;
    private OrchidResources resources;

    private Map<String, String> iconMap;

    @Inject
    public RenderIndex(OrchidContext context, OrchidResources resources) {
        this.context = context;
        this.resources = resources;
        this.iconMap = new HashMap<>();

        iconMap.put("css", "/assets/svg/css.svg");
        iconMap.put("csv", "/assets/svg/csv.svg");
        iconMap.put("html", "/assets/svg/html.svg");
        iconMap.put("jpg", "/assets/svg/jpg.svg");
        iconMap.put("js", "/assets/svg/js.svg");
        iconMap.put("json", "/assets/svg/json.svg");
        iconMap.put("png", "/assets/svg/png.svg");
        iconMap.put("xml", "/assets/svg/xml.svg");

        iconMap.put("file", "/assets/svg/folder.svg");
        iconMap.put("folder", "/assets/svg/folder.svg");
    }

    public void render(HttpExchange t, File targetFile, String targetPath) throws IOException {
        Headers responseHeaders = t.getResponseHeaders();

        String content = "";

        if (targetFile.isDirectory()) {
            File[] files = targetFile.listFiles();

            if (files != null) {
                JSONArray jsonDirs = new JSONArray();
                JSONArray jsonFiles = new JSONArray();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd-hh:mm:ss z", Locale.getDefault());

                for (File file : files) {
                    JSONObject newFile = new JSONObject();
                    newFile.put("url", OrchidUtils.applyBaseUrl(StringUtils.strip(targetPath, "/") + "/" + file.getName()));
                    newFile.put("name", file.getName());
                    newFile.put("size", file.length());
                    newFile.put("date", formatter.format(new Date(file.lastModified())));

                    if (file.isDirectory()) {
                        newFile.put("icon", iconMap.get("folder"));
                        jsonDirs.put(newFile);
                    }
                    else {
                        newFile.put("icon",
                                iconMap.containsKey(FilenameUtils.getExtension(file.getName()))
                                        ? iconMap.get(FilenameUtils.getExtension(file.getName()))
                                        : iconMap.get("file")
                        );
                        jsonFiles.put(newFile);
                    }
                }

                JSONObject page = new JSONObject();
                page.put("title", "List of files/dirs under " + targetPath);
                page.put("path", targetPath);
                page.put("dirs", jsonDirs);
                page.put("files", jsonFiles);

                JSONObject object = new JSONObject(context.getRoot().toMap());
                object.put("page", page);

                OrchidResource resource = resources.getResourceEntry("templates/server/directoryListing.twig");

                if (resource != null) {
                    Clog.i("Server Directory Listing resource not null:" + targetFile.getAbsolutePath());
                    responseHeaders.set("Content-Type", "text/html; charset=UTF-8");
                    content = context.getTheme().compile(resource.getReference().getExtension(), resource.getContent(), object.toString(2));
                }
                else {
                    responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
                    content = object.toString(2);
                }
            }
        }

        Clog.i("Rendering Index: #{$1}", new Object[]{targetPath});
        t.sendResponseHeaders(200, content.length());
        IOUtils.write(content, t.getResponseBody());
        t.getResponseBody().close();
    }
}
