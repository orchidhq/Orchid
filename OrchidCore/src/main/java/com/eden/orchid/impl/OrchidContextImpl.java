package com.eden.orchid.impl;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.CompilerService;
import com.eden.orchid.api.events.EventService;
import com.eden.orchid.api.generators.OrchidGenerators;
import com.eden.orchid.api.indexing.IndexService;
import com.eden.orchid.api.options.OrchidFlags;
import com.eden.orchid.api.options.OrchidOptions;
import com.eden.orchid.api.resources.ResourceService;
import com.eden.orchid.api.tasks.OrchidTasks;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.ThemeService;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import com.google.inject.Injector;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Getter
public final class OrchidContextImpl implements OrchidContext {

    private Injector injector;

    private JSONObject root;
    private JSONObject optionsData;
    private JSONObject configData;

    private OrchidTasks orchidTasks;
    private OrchidFlags flags;
    private OrchidOptions options;
    private OrchidGenerators generators;

    private CompilerService compilerService;
    private ThemeService themeService;
    private EventService eventService;
    private IndexService indexService;
    private ResourceService resourceService;

    @Inject
    public OrchidContextImpl(
            Injector injector,
            OrchidTasks orchidTasks,
            OrchidGenerators generators,
            OrchidOptions options,

            CompilerService compilerService,
            ThemeService themeService,
            EventService eventService,
            IndexService indexService,
            ResourceService resourceService
    ) {
        this.injector = injector;
        this.orchidTasks = orchidTasks;
        this.flags = OrchidFlags.getInstance();
        this.generators = generators;
        this.options = options;

        this.root = new JSONObject();

        this.compilerService = compilerService;
        this.themeService = themeService;
        this.eventService = eventService;
        this.indexService = indexService;
        this.resourceService = resourceService;
    }

    @Override
    public boolean runTask(Theme defaultTheme, String taskName) {
        themeService.setDefaultTheme(defaultTheme);
        orchidTasks.run(taskName);
        return true;
    }

    @Override
    public void build() {
        eventService.broadcast(Orchid.Events.BUILD_START);
        themeService.clearThemes();

        optionsData = options.loadOptions();

        // Create theme menus
        JSONElement menuElement = query("options.menu");
        if (OrchidUtils.elementIsArray(menuElement)) {
            getTheme().createMenu(null, (JSONArray) menuElement.getElement());
        }
        else if (OrchidUtils.elementIsObject(menuElement)) {
            getTheme().createMenus((JSONObject) menuElement.getElement());
        }

        configData = (optionsData.has("config")) ? optionsData.getJSONObject("config") : new JSONObject();

        generators.startIndexing();

        // Add discovered assets
        // TODO: Leave this up to themes/pages/components to add their own assets rather than do it by force
        // TODO: Alternatively, allow this behavior as a flag?
        for (OrchidPage style : getIndex().find("assets/js")) {
            getTheme().addJs(style);
        }
        for (OrchidPage style : getIndex().find("assets/css")) {
            getTheme().addCss(style);
        }

        generators.startGeneration();
        eventService.broadcast(Orchid.Events.BUILD_FINISH);
    }

    @Override
    public JSONElement query(String pointer) {
        if (!EdenUtils.isEmpty(pointer)) {
            if (pointer.startsWith("options.")) {
                return new JSONElement(optionsData).query(pointer.replace("options.", ""));
            }
        }

        return new JSONElement(root).query(pointer);
    }

    @Override
    public Map<String, Object> getSiteData(Object... data) {
        Map<String, Object> siteData = new HashMap<>();

        Map<String, ?> root = getRoot().toMap();

        for (String key : root.keySet()) {
            siteData.put(key, root.get(key));
        }

        if (data != null && data.length > 0) {
            if (data[0] instanceof OrchidPage) {
                OrchidPage page = (OrchidPage) data[0];
                siteData.put("page", page);
                siteData.put(page.getKey(), page);
            }
            else if (data[0] instanceof OrchidComponent) {
                OrchidComponent component = (OrchidComponent) data[0];
                siteData.put("component", component);
                siteData.put(component.getKey(), component);
            }
            else if (data[0] instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) data[0];
                siteData.put("data", jsonObject);

                for (String key : jsonObject.keySet()) {
                    siteData.put(key, jsonObject.get(key));
                }
            }
            else if (data[0] instanceof Map) {
                Map<String, ?> map = (Map<String, ?>) data[0];
                siteData.put("data", map);

                for (String key : map.keySet()) {
                    siteData.put(key, map.get(key));
                }
            }
            else if (data[0] instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) data[0];
                siteData.put("data", jsonArray);
            }
            else if (data[0] instanceof Collection) {
                Collection collection = (Collection) data[0];
                siteData.put("data", collection);
            }
        }

        siteData.put("index", getIndex());
        siteData.put("options", optionsData.toMap());

        return siteData;
    }

// Implement Service Delegate Interfaces
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public CompilerService getCompilerService() {
        return compilerService;
    }

    @Override
    public ThemeService getThemeService() {
        return themeService;
    }

    @Override
    public EventService getEventService() {
        return eventService;
    }

    @Override
    public IndexService getIndexService() {
        return indexService;
    }

    @Override
    public ResourceService getResourceService() {
        return resourceService;
    }

}
