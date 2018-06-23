package com.eden.orchid.api.server;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidCollection;
import com.eden.orchid.api.options.OptionsExtractor;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.server.admin.AdminList;
import com.eden.orchid.api.tasks.OrchidCommand;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.api.theme.assets.AssetHolder;
import com.eden.orchid.api.theme.assets.AssetHolderDelegate;
import com.eden.orchid.api.theme.assets.AssetPage;
import com.eden.orchid.utilities.OrchidUtils;
import com.google.inject.Provider;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrchidView implements OptionsHolder, AssetHolder {

    @Getter @Setter private String layout;

    @Getter @Setter private String title;
    @Getter @Setter private String[] breadcrumbs;

    @Getter private final OrchidContext context;
    @Getter private final OrchidController controller;

    @Getter @Setter protected AssetHolder assets;
    private boolean hasAddedAssets;

    @Getter private Provider<OrchidServer> server;
    private Provider<Set<AdminList>> adminLists;

    @Getter private String[] views;
    @Getter @Setter private Map<String, ?> data;

    public OrchidView(OrchidContext context, OrchidController controller, String... views) {
        this(context, controller, null, views);
    }

    public OrchidView(OrchidContext context, OrchidController controller, Map<String, ?> data, String... views) {
        this.context = context;
        this.controller = controller;
        this.views = views;
        this.data = data;

        this.title = "Admin";

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

    public final void addAssets() {
        if(!hasAddedAssets) {
            loadAssets();
            hasAddedAssets = true;
        }
    }

    protected void loadAssets() {

    }

    @Override
    public final List<AssetPage> getScripts() {
        addAssets();
        List<AssetPage> scripts = new ArrayList<>();
        scripts.addAll(context.getAdminTheme().getScripts());
        scripts.addAll(assets.getScripts());

        return scripts;
    }

    @Override
    public final List<AssetPage> getStyles() {
        addAssets();
        List<AssetPage> styles = new ArrayList<>();
        styles.addAll(context.getAdminTheme().getStyles());
        styles.addAll(assets.getStyles());

        return styles;
    }

// View renderer
//----------------------------------------------------------------------------------------------------------------------

    public final String renderView() {
        List<String> viewList = Arrays.stream(this.views)
                .map(s -> Clog.format("templates/server/admin/{}.peb", OrchidUtils.normalizePath(s)))
                .collect(Collectors.toList());

        OrchidResource resource = context.locateTemplate(viewList);
        if(resource != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("view", this);
            data.put("controller", controller);
            data.put("cxt", context);
            data.put("httpServerPort", server.get().getHttpServerPort());
            data.put("websocketPort", server.get().getWebsocketPort());
            data.put("adminTheme", context.getAdminTheme());
            data.put("site", context.getSite());
            data.put("optionsExtractor", context.resolve(OptionsExtractor.class));

            if (this.data != null) {
                data.putAll(this.data);
            }

            return context.compile(resource.getReference().getExtension(), resource.getContent(), data);
        }

        return Clog.format("View '{}' not found", viewList);
    }

// Other
//----------------------------------------------------------------------------------------------------------------------

    public List<AdminList> getAdminLists() {
        return this.adminLists.get()
                .stream()
                .sorted(Comparator.comparing(o -> o.getListClass().getSimpleName()))
                .collect(Collectors.toList());
    }

    public List<AdminList> getImportantAdminLists() {
        return getAdminLists()
                .stream()
                .filter(AdminList::isImportantType)
                .collect(Collectors.toList());
    }

    public AdminList getGenerators() {
        for(AdminList list : adminLists.get()) {
            if(list.getKey().equals("OrchidGenerator")) {
                return list;
            }
        }

        return null;
    }

    public String getDescriptionLink(Object o) {
        Class className = (o instanceof Class) ? (Class) o : o.getClass();

        try {
            return context.getBaseUrl() + "/admin/describe?className=" + URLEncoder.encode(className.getName(), "UTF-8");
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public List<OrchidTask> getTasks() {
        return this.context.resolveSet(OrchidTask.class)
                .stream()
                .sorted(Comparator.comparing(OrchidTask::getName))
                .collect(Collectors.toList());
    }

    public List<OrchidCommand> getCommands() {
        return this.context.resolveSet(OrchidCommand.class)
                .stream()
                .sorted(Comparator.comparing(OrchidCommand::getKey))
                .collect(Collectors.toList());
    }

    public List<OrchidCollection> getCollections() {
        return this.context.getCollections()
                .stream()
                .sorted(Comparator.comparing(OrchidCollection::getCollectionType))
                .collect(Collectors.toList());
    }

    public List<OrchidCollection> getRelatedCollections(String collectionType, String collectionId) {
        Stream<? extends OrchidCollection> stream = this.context.getCollections().stream().filter(Objects::nonNull);

        if(!EdenUtils.isEmpty(collectionType) && !EdenUtils.isEmpty(collectionId)) {
            stream = stream.filter(collection -> collectionType.equals(collection.getCollectionType()));
        }
        else if(!EdenUtils.isEmpty(collectionType)) {
            stream = stream.filter(collection -> collectionType.equals(collection.getCollectionType()));
        }
        else if(!EdenUtils.isEmpty(collectionId)) {
            OrchidCollection estimatedCollection = stream.findFirst().orElse(null);
            if(estimatedCollection != null) {
                stream = stream.filter(collection -> estimatedCollection.getCollectionType().equals(collection.getCollectionType()));
            }
            else {
                stream = this.context.getCollections().stream().filter(Objects::nonNull);
            }
        }

        return stream.sorted(Comparator.comparing(OrchidCollection::getCollectionType)).collect(Collectors.toList());
    }

}
