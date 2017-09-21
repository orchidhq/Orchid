package com.eden.orchid.impl.server.admin;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsDescription;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.server.OrchidController;
import com.eden.orchid.api.server.OrchidRequest;
import com.eden.orchid.api.server.OrchidResponse;
import com.eden.orchid.api.server.OrchidServer;
import com.eden.orchid.api.server.admin.AdminList;
import com.eden.orchid.api.server.annotations.Get;
import com.google.inject.Provider;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Singleton
public class AdminController extends OrchidController {

    private OrchidContext context;
    private Provider<OrchidServer> server;
    private Set<AdminList> adminLists;

    @Inject
    public AdminController(OrchidContext context, Provider<OrchidServer> server, Set<AdminList> adminLists) {
        super(1000);
        this.context = context;
        this.server = server;
        this.adminLists = adminLists;
    }

    @Get(path = "/")
    public OrchidResponse doNothing(OrchidRequest request) {
        Clog.v("Rendering admin index page");
        OrchidResource resource = context.getResourceEntry("templates/server/admin/admin.twig");
        String content = "";
        if (resource != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("controller", this);
            data.put("cxt", context);
            data.put("adminLists", adminLists);
            data.put("httpServerPort", server.get().getHttpServerPort());
            data.put("websocketPort", server.get().getWebsocketPort());
            data.put("theme", context.getAdminTheme());

            content = context.compile(resource.getReference().getExtension(), resource.getContent(), data);
        }
        return new OrchidResponse(content);
    }

    @Get(path = "/lists/:name")
    public OrchidResponse renderList(OrchidRequest request, String name) {
        AdminList foundList = null;
        for (AdminList list : adminLists) {
            if (list.getKey().equalsIgnoreCase(name)) {
                foundList = list;
                break;
            }
        }

        if (foundList != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("controller", this);
            data.put("cxt", context);
            data.put("adminLists", adminLists);
            data.put("httpServerPort", server.get().getHttpServerPort());
            data.put("websocketPort", server.get().getWebsocketPort());
            data.put("adminList", foundList);
            data.put("theme", context.getAdminTheme());

            OrchidResource resource = context.getResourceEntry("templates/server/admin/adminList.twig");
            return new OrchidResponse(context.compile(resource.getReference().getExtension(), resource.getContent(), data));
        }
        else {
            return new OrchidResponse("List not found");
        }
    }

    @Get(path = "/lists/:name/:id")
    public OrchidResponse renderListItem(OrchidRequest request, String name, String id) {
        AdminList foundList = null;
        for (AdminList list : adminLists) {
            if (list.getKey().equalsIgnoreCase(name)) {
                foundList = list;
                break;
            }
        }

        if (foundList != null) {
            Object listItem = foundList.getItem(id);
            if (listItem != null) {
                Map<String, Object> data = new HashMap<>();
                data.put("controller", this);
                data.put("cxt", context);
                data.put("adminLists", adminLists);
                data.put("httpServerPort", server.get().getHttpServerPort());
                data.put("websocketPort", server.get().getWebsocketPort());
                data.put("adminList", foundList);
                data.put("listItem", listItem);
                data.put("theme", context.getAdminTheme());

                OrchidResource resource = context.getResourceEntry("templates/server/admin/adminListItem.twig");
                return new OrchidResponse(context.compile(resource.getReference().getExtension(), resource.getContent(), data));
            }
            else {
                return new OrchidResponse("List item not found");
            }
        }
        else {
            return new OrchidResponse("List not found");
        }
    }

    public boolean hasOptions(Object object) {
        return object instanceof OptionsHolder;
    }

    public List<OptionsDescription> getOptions(Object object) {
        if(object instanceof OptionsHolder) {
            return ((OptionsHolder) object).describeOptions(context);
        }

        return new ArrayList<>();
    }
}
