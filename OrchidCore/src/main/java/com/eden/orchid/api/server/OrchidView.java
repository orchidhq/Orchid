package com.eden.orchid.api.server;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidCollection;
import com.eden.orchid.api.options.Descriptive;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.render.RenderService;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.api.server.admin.AdminList;
import com.eden.orchid.api.tasks.OrchidCommand;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.api.theme.AbstractTheme;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.assets.AssetManagerDelegate;
import com.eden.orchid.api.theme.assets.CssPage;
import com.eden.orchid.api.theme.assets.JsPage;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;

import javax.annotation.Nonnull;
import javax.inject.Provider;
import javax.inject.Inject;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Description(value = "A representation of a server-rendered page, accessible when running Orchid's development server.", name = "View")
public final class OrchidView extends OrchidPage {

    public enum Type {
        Page, Fullscreen;
    }

    private String[] breadcrumbs;
    private final OrchidController controller;
    @Inject
    private Provider<Set<AdminList>> adminLists;
    @Inject
    private Provider<OrchidServer> orchidServerProvider;
    private Object params;
    private Type type;

    public OrchidView(OrchidContext context, OrchidController controller, String... views) {
        this(context, controller, "Admin", new HashMap<>(), views);
    }

    public OrchidView(
            OrchidContext context,
            OrchidController controller,
            String title,
            Map<String, Object> data,
            String... views
    ) {
        super(
                new StringResource(new OrchidReference(context, "view.html"), "", data),
                RenderService.RenderMode.TEMPLATE,
                "view",
                title
        );
        this.controller = controller;
        this.layout = "layoutBase";
        this.template = views;
        context.injectMembers(this);
    }

// Assets
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void loadAssets(@Nonnull AssetManagerDelegate delegate) {
        delegate.addJs("assets/js/shadowComponents.js");
    }

    @Override
    public AbstractTheme getTheme() {
        return context.getAdminTheme();
    }

    // View renderer
//----------------------------------------------------------------------------------------------------------------------
    @Override
    @Nonnull
    public String getTemplateBase() {
        return "server/" + super.getTemplateBase();
    }

    @Override
    public String getLayoutBase() {
        return "server/" + super.getLayoutBase();
    }

    // Other
//----------------------------------------------------------------------------------------------------------------------
    public List<AdminList> getAdminLists() {
        return this.adminLists.get().stream().sorted(Comparator.comparing(o -> o.getListClass().getSimpleName())).collect(Collectors.toList());
    }

    public List<AdminList> getImportantAdminLists() {
        return getAdminLists().stream().filter(AdminList::isImportantType).collect(Collectors.toList());
    }

    public AdminList getGenerators() {
        for (AdminList list : adminLists.get()) {
            if (list.getKey().equals("OrchidGenerator")) {
                return list;
            }
        }
        return null;
    }

    public String getDescriptionLink(Object o) {
        Class className = (o instanceof Class) ? (Class) o : o.getClass();
        try {
            return context.getBaseUrl() + "/admin/describe?className=" + URLEncoder.encode(className.getName(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getDescriptiveName(Object o) {
        return Descriptive.getDescriptiveName((o instanceof Class) ? (Class) o : o.getClass());
    }

    public String getDescription(Object o) {
        return Descriptive.getDescription((o instanceof Class) ? (Class) o : o.getClass());
    }

    public String getDescriptionSummary(Object o) {
        return Descriptive.getDescriptionSummary((o instanceof Class) ? (Class) o : o.getClass());
    }

    public List<OrchidTask> getTasks() {
        return this.context.resolveSet(OrchidTask.class).stream().sorted(Comparator.comparing(OrchidTask::getName)).collect(Collectors.toList());
    }

    public List<OrchidCommand> getCommands() {
        return this.context.resolveSet(OrchidCommand.class).stream().sorted(Comparator.comparing(OrchidCommand::getKey)).collect(Collectors.toList());
    }

    public List<OrchidCollection> getCollections() {
        return this.context.getCollections().stream().sorted(Comparator.comparing(OrchidCollection::getCollectionType)).collect(Collectors.toList());
    }

    public boolean isFullscreen() {
        return this.type != null && this.type == Type.Fullscreen;
    }

    public String[] getBreadcrumbs() {
        return this.breadcrumbs;
    }

    public void setBreadcrumbs(final String[] breadcrumbs) {
        this.breadcrumbs = breadcrumbs;
    }

    public OrchidController getController() {
        return this.controller;
    }

    public void setAdminLists(final Provider<Set<AdminList>> adminLists) {
        this.adminLists = adminLists;
    }

    public Object getParams() {
        return this.params;
    }

    public void setParams(final Object params) {
        this.params = params;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(final Type type) {
        this.type = type;
    }

    public List<AdminPageRoute> getAdminPages() {
        return CollectionsKt.sortedBy(
                orchidServerProvider.get().getServer().getAdminRoutes(),
                it -> it.getTitle()
        );
    }

    public List<AdminMenuItem> getAdminMenuItems() {
        return Arrays.asList(
                new AdminMenuItem("Content", "list", "server/includes/contentPanel"),
                new AdminMenuItem("Collections", "thumbnails", "server/includes/collectionsPanel"),
                new AdminMenuItem("Plugin Docs", "file-edit", "server/includes/pluginDocsPanel"),
                new AdminMenuItem("Manage Site", "settings", "server/includes/managePanel"),
                new AdminMenuItem("Get Help", "question", "server/includes/helpPanel")
        );
    }
}
