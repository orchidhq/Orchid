package com.eden.orchid.server.impl.controllers.admin;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.OrchidFlags;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resourceSource.DefaultResourceSource;
import com.eden.orchid.api.resources.resourceSource.LocalResourceSource;
import com.eden.orchid.api.resources.resourceSource.OrchidResourceSource;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.server.OrchidServer;
import com.eden.orchid.server.api.OrchidController;
import com.eden.orchid.server.api.OrchidRequest;
import com.eden.orchid.server.api.OrchidResponse;
import com.eden.orchid.server.api.methods.Get;
import com.eden.orchid.utilities.ObservableTreeSet;
import com.eden.orchid.utilities.OrchidUtils;
import com.google.inject.Provider;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

            content = context.compile(resource.getReference().getExtension(), resource.getContent(), data);
        }
        return new OrchidResponse(content);
    }

    @Get(path="/lists/:name")
    public OrchidResponse renderList(OrchidRequest request, String name) {
        Clog.v("calling /admin/lists/:name");
        OrchidResource resource = resources.getResourceEntry("templates/server/admin/lists/" + name + ".twig");
        if(resource != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("httpServerPort", server.get().getHttpServerPort());
            data.put("websocketPort", server.get().getWebsocketPort());

            data.put(name, getList(name));

            return new OrchidResponse(context.compile(resource.getReference().getExtension(), resource.getContent(), data));
        }
        return new OrchidResponse("List not found");
    }

    private Collection<?> getList(String name) {
        switch(name.toLowerCase()) {
            case "compilers":       return new ObservableTreeSet<>(OrchidUtils.resolveSet(context, OrchidCompiler.class));
            case "generators":      return new ObservableTreeSet<>(OrchidUtils.resolveSet(context, OrchidGenerator.class));
            case "options":         return OrchidFlags.getInstance().getFlags();
            case "resourceSources":
                ObservableTreeSet<OrchidResourceSource> toReturn = new ObservableTreeSet<>();
                toReturn.addAll(OrchidUtils.resolveSet(context, LocalResourceSource.class));
                toReturn.addAll(OrchidUtils.resolveSet(context, DefaultResourceSource.class));
                return new ObservableTreeSet<>(toReturn);
            case "tasks":           return new ObservableTreeSet<>(OrchidUtils.resolveSet(context, OrchidTask.class));
            case "themes":          return new ObservableTreeSet<>(OrchidUtils.resolveSet(context, Theme.class));
        }

        return null;
    }

}
