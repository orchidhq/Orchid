package com.eden.orchid.api.server;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.server.admin.AdminList;
import com.google.inject.Provider;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Accessors(chain = true, fluent = true)
public final class OrchidView {

    private final OrchidContext context;
    private final OrchidController controller;

    private Provider<OrchidServer> server;
    private Provider<Set<AdminList>> adminLists;

    @Getter private String view;
    @Getter @Setter private Map<String, ?> data;

    public OrchidView(OrchidContext context, OrchidController controller, String view) {
        this(context, controller, view, null);
    }

    public OrchidView(OrchidContext context, OrchidController controller, String view, Map<String, ?> data) {
        this.context = context;
        this.controller = controller;
        this.view = view;
        this.data = data;
        context.getInjector().injectMembers(this);
    }

// Injected members
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public void setAdminLists(Provider<Set<AdminList>> adminLists) {
        this.adminLists = adminLists;
    }

    @Inject
    public void setServer(Provider<OrchidServer> server) {
        this.server = server;
    }

// View renderer
//----------------------------------------------------------------------------------------------------------------------

    public String renderView() {
        OrchidResource resource = context.getResourceEntry(Clog.format("templates/server/admin/{}.twig", this.view));
        if(resource != null) {

            Map<String, Object> data = new HashMap<>();
            data.put("view", this);
            data.put("controller", controller);
            data.put("cxt", context);
            data.put("adminLists", adminLists.get());
            data.put("httpServerPort", server.get().getHttpServerPort());
            data.put("websocketPort", server.get().getWebsocketPort());
            data.put("adminTheme", context.getAdminTheme());
            data.put("site", context.getSite());

            if (this.data != null) {
                data.putAll(this.data);
            }

            return context.compile(resource.getReference().getExtension(), resource.getContent(), data);
        }

        return Clog.format("View '{}' not found", this.view);
    }

}
