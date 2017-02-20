package com.eden.orchid.server.server;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.OrchidResources;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;

@Singleton
public class Render404 {

    private OrchidContext context;
    private OrchidResources resources;

    @Inject
    public Render404(OrchidContext context, OrchidResources resources) {
        this.context = context;
        this.resources = resources;
    }

    public void render(HttpExchange t, File targetFile, String targetPath) throws IOException {
        Headers responseHeaders = t.getResponseHeaders();

        JSONObject page = new JSONObject();
        page.put("title", "404 - " + targetPath);
        page.put("path", targetPath);

        JSONObject object = new JSONObject(context.getRoot().toMap());
        object.put("page", page);

        OrchidResource resource = resources.getResourceEntry("templates/server/404.twig");

        String content = "";
        if(resource != null) {
            Clog.i("Rendering 404 template: #{$1}", new Object[]{targetPath});
            responseHeaders.set("Content-Type", "text/html; charset=UTF-8");
            content = context.getTheme().compile(resource.getReference().getExtension(), resource.getContent(), object.toString(2));
        }

        Clog.i("Rendering 404: #{$1}", new Object[]{targetPath});
        t.sendResponseHeaders(200, content.length());
        IOUtils.write(content, t.getResponseBody());
        t.getResponseBody().close();
    }

}
