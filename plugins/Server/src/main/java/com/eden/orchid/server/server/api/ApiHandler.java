package com.eden.orchid.server.server.api;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.server.server.RequestHandler;
import com.eden.orchid.server.server.StaticServer;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.IOException;

public class ApiHandler implements RequestHandler {

    private OrchidContext context;
    private OrchidResources resources;

    @Inject
    public ApiHandler(OrchidContext context, OrchidResources resources) {
        this.context = context;
        this.resources = resources;
    }

    public boolean canHandle(HttpExchange t, String targetPath) {
        if(t.getRequestMethod().equalsIgnoreCase("POST")) {
            if(targetPath.equalsIgnoreCase("/api/rebuild")) {
                return true;
            }
            else if(targetPath.equalsIgnoreCase("/api/session/end")) {
                return true;
            }
        }

        return false;
    }

    public void render(HttpExchange t, String targetPath) throws IOException {
        if(targetPath.equalsIgnoreCase("/api/rebuild")) {
            Clog.v("API event to rebuild");
            context.broadcast(Orchid.Events.FILES_CHANGED);
            JSONObject data = new JSONObject();
            data.put("message", "site rebuilt");
            StaticServer.renderJSON(t, data);
        }
        else if(targetPath.equalsIgnoreCase("/api/session/end")) {
            Clog.v("API to end session");
            context.broadcast(Orchid.Events.FILES_CHANGED);
            JSONObject data = new JSONObject();
            data.put("message", "session ended");
            StaticServer.renderJSON(t, data);
            context.broadcast(Orchid.Events.END_SESSION);
        }
    }
}
