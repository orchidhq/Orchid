package com.eden.orchid.server.impl.controllers.admin;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.server.api.methods.Get;
import com.eden.orchid.server.api.OrchidController;
import com.eden.orchid.server.api.OrchidRequest;
import com.eden.orchid.server.api.OrchidResponse;
import com.eden.orchid.server.OrchidServer;
import com.google.inject.Provider;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AdminController implements OrchidController {

    private OrchidContext context;
    private OrchidResources resources;
    private Provider<OrchidServer> server;

    @Inject
    public AdminController(OrchidContext context, OrchidResources resources, Provider<OrchidServer> server) {
        this.context = context;
        this.resources = resources;
        this.server = server;
    }

    @Get(path="/")
    public OrchidResponse doNothing(OrchidRequest request) {
        Clog.v("calling /admin");
        OrchidResource resource = resources.getResourceEntry("templates/server/admin/admin.twig");
        String content = "";
        if(resource != null) {

            JSONObject data = new JSONObject();
            data.put("httpServerPort", server.get().getHttpServerPort());
            data.put("websocketPort", server.get().getWebsocketPort());

            content = context.getTheme().compile(resource.getReference().getExtension(), resource.getContent(), data);
        }
        return new OrchidResponse(content);
    }
}
