package com.eden.orchid.impl;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.events.EventService;
import com.eden.orchid.api.events.FilterService;
import com.eden.orchid.api.generators.OrchidGenerators;
import com.eden.orchid.api.indexing.OrchidIndex;
import com.eden.orchid.api.options.OrchidOptions;
import com.eden.orchid.api.resources.resourceSource.DefaultResourceSource;
import com.eden.orchid.api.resources.resourceSource.OrchidResourceSource;
import com.eden.orchid.api.tasks.OrchidTasks;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.impl.indexing.OrchidCompositeIndex;
import com.eden.orchid.impl.indexing.OrchidExternalIndex;
import com.eden.orchid.impl.indexing.OrchidRootInternalIndex;
import com.eden.orchid.utilities.OrchidUtils;
import com.google.inject.Injector;
import com.sun.javadoc.RootDoc;
import lombok.Data;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Data
@Singleton
public final class OrchidContextImpl implements OrchidContext {

    private Injector injector;

    private JSONObject root;
    private Theme theme;
    private RootDoc rootDoc;

    private OrchidTasks orchidTasks;
    private OrchidOptions options;
    private OrchidGenerators generators;
    private EventService eventService;
    private FilterService filterService;

    @Inject
    public OrchidContextImpl(Injector injector, OrchidTasks orchidTasks, OrchidOptions options, OrchidGenerators generators, EventService eventService, FilterService filterService) {
        this.injector = injector;
        this.orchidTasks = orchidTasks;
        this.options = options;
        this.generators = generators;
        this.eventService = eventService;
        this.filterService = filterService;
    }

    @Override
    public void bootstrap(Map<String, String[]> optionsMap, RootDoc rootDoc) {
        eventService.broadcast(Orchid.Events.INIT_COMPLETE);

        this.root = new JSONObject();
        this.rootDoc = rootDoc;
        root.put("options", new JSONObject());

        options.parseOptions(optionsMap, root.getJSONObject("options"));
        eventService.broadcast(Orchid.Events.OPTIONS_PARSED, root.getJSONObject("options"));

        eventService.broadcast(Orchid.Events.BOOTSTRAP_COMPLETE);
    }

    @Override
    public boolean runTask(String taskName) {
        if (shouldContinue()) {
            reorderResourceSources();
            Clog.i("Using Theme: #{$1}", new Object[]{theme.getClass().getName()});
            eventService.broadcast(Orchid.Events.THEME_SET, getTheme());
            orchidTasks.run(taskName);
            return true;
        }
        else {
            return false;
        }
    }

    private boolean shouldContinue() {
        return options.shouldContinue() && (theme != null) && theme.shouldContinue();
    }

    @Override
    public void build() {
        eventService.broadcast(Orchid.Events.BUILD_START);
        root.put("index", new JSONObject());
        generators.startIndexing();
        generators.startGeneration();
        eventService.broadcast(Orchid.Events.BUILD_FINISH);
    }

    @Override
    public JSONElement query(String pointer) { return new JSONElement(root).query(pointer); }

    @Override
    public void broadcast(String event, Object... args) {
        eventService.broadcast(event, args);
    }

    private void reorderResourceSources() {
        Theme theme = getTheme();

        reorderThemes();

        for (OrchidResourceSource source : OrchidUtils.resolveSet(this, DefaultResourceSource.class)) {
            if (source instanceof Theme) {
                if (!source.getClass().isAssignableFrom(theme.getClass())) {
                    source.setPriority(-1);
                }
            }
        }
    }

    private void reorderThemes() {
        Class<?> superclass = getTheme().getClass();
        int i = 0;

        // find the highest priority of any Theme
        int highestThemePriority = 0;
        for (OrchidResourceSource resourceSourceEntry : OrchidUtils.resolveSet(this, OrchidResourceSource.class)) {
            if (resourceSourceEntry instanceof Theme) {
                highestThemePriority = Math.max(highestThemePriority, resourceSourceEntry.getPriority());
            }
        }

        // Go through all Themes and set each parent theme as the next-highest Theme priority
        while (!superclass.equals(Theme.class)) {
            for (OrchidResourceSource resourceSourceEntry : OrchidUtils.resolveSet(this, OrchidResourceSource.class)) {
                if (resourceSourceEntry instanceof Theme) {
                    Theme theme = (Theme) resourceSourceEntry;
                    if (theme.getClass().equals(superclass)) {
                        theme.setPriority((highestThemePriority) - i);
                        break;
                    }
                }
            }

            i++;
            superclass = superclass.getSuperclass();
        }
    }

    @Override
    public Map<String, Object> getSiteData(Object... data) {
        Map<String, Object> siteData = new HashMap<>();

        Map<String, ?> root = getRoot().toMap();

        for (String key : root.keySet()) {
            siteData.put(key, root.get(key));
        }

        if(data != null && data.length > 0) {
            if(data[0] instanceof OrchidPage) {
                OrchidPage page = (OrchidPage) data[0];
                siteData.put("page", page);

                if(!EdenUtils.isEmpty(page.getType()) && !page.getType().equalsIgnoreCase("page")) {
                    siteData.put(page.getType(), page);
                }

                Map<String, OrchidComponent> pageComponents = page.getComponents();

                for (Map.Entry<String, OrchidComponent> componentEntry : pageComponents.entrySet()) {
                    siteData.put(componentEntry.getKey(), componentEntry.getValue());
                }
            }
            else if(data[0] instanceof OrchidComponent) {
                OrchidComponent component = (OrchidComponent) data[0];
                siteData.put("component", component);

                if(!EdenUtils.isEmpty(component.getAlias()) && !component.getAlias().equalsIgnoreCase("component")) {
                    siteData.put(component.getAlias(), component);
                }
            }
        }

        siteData.put("root", root);
        siteData.put("theme", getTheme());
        siteData.put("index", getIndex());

        return siteData;
    }

    public OrchidIndex getIndex() {
        return this.generators.getInternalIndex();
    }

    public OrchidRootInternalIndex getInternalIndex() {
        return this.generators.getInternalIndex();
    }

    public OrchidExternalIndex getExternalIndex() {
        return this.generators.getExternalIndex();
    }

    public OrchidCompositeIndex getCompositeIndex() {
        return this.generators.getCompositeIndex();
    }
}
