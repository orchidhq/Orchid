package com.eden.orchid.api.server;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.server.admin.AdminList;
import com.eden.orchid.api.theme.assets.AssetHolder;
import com.eden.orchid.api.theme.assets.AssetHolderDelegate;
import com.eden.orchid.api.theme.assets.AssetPage;
import com.eden.orchid.utilities.OrchidUtils;
import com.google.inject.Provider;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrchidView implements OptionsHolder, AssetHolder {

    @Getter @Setter private String layout;

    @Getter @Setter private String title;
    @Getter @Setter private String[] breadcrumbs;

    @Getter private final OrchidContext context;
    @Getter private final OrchidController controller;

    @Getter @Setter protected AssetHolder assets;

    @Getter private Provider<OrchidServer> server;
    @Getter private Provider<Set<AdminList>> adminLists;

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

        this.title = OrchidUtils.camelcaseToTitleCase(view);

        this.assets = new AssetHolderDelegate(context, this, "page");
        this.layout = "templates/server/admin/base.peb";

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

// Assets
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public AssetHolder getAssetHolder() {
        return assets;
    }

    @Override
    public void addAssets() {

    }

    @Override
    public List<AssetPage> getScripts() {
        List<AssetPage> scripts = new ArrayList<>();
        scripts.addAll(context.getAdminTheme().getScripts());
        scripts.addAll(assets.getScripts());

        return scripts;
    }

    @Override
    public List<AssetPage> getStyles() {
        List<AssetPage> styles = new ArrayList<>();
        styles.addAll(context.getAdminTheme().getStyles());
        styles.addAll(assets.getStyles());

        return styles;
    }

// View renderer
//----------------------------------------------------------------------------------------------------------------------

    public final String renderView() {
        OrchidResource resource = context.getResourceEntry(Clog.format("templates/server/admin/{}.peb", OrchidUtils.normalizePath(this.view)));
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
