package com.eden.orchid.server.server.admin;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.server.server.RequestHandler;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.IOException;

public class AdminHandler implements RequestHandler {

    private OrchidContext context;
    private OrchidResources resources;

    @Inject
    public AdminHandler(OrchidContext context, OrchidResources resources) {
        this.context = context;
        this.resources = resources;
    }

    public boolean canHandle(HttpExchange t, String targetPath) {
        return targetPath.equalsIgnoreCase("/admin");
    }

    public void render(HttpExchange t, String targetPath) throws IOException {
        Headers responseHeaders = t.getResponseHeaders();

        JSONObject page = new JSONObject();
        page.put("title", "Orchid Admin - " + targetPath);
        page.put("path", targetPath);

        JSONObject object = new JSONObject(context.getRoot().toMap());
        object.put("page", page);

        OrchidResource resource = resources.getResourceEntry("templates/server/admin" + targetPath + ".twig");

        String content = "";
        if(resource != null) {
            content = context.getTheme().compile(resource.getReference().getExtension(), resource.getContent(), object.toString(2));
        }

        Clog.i("Rendering admin page: #{$1}", new Object[]{targetPath});
        responseHeaders.set("Content-Type", "text/html; charset=UTF-8");
        t.sendResponseHeaders(200, content.length());
        IOUtils.write(content, t.getResponseBody());
        t.getResponseBody().close();
    }
}
