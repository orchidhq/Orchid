package com.eden.orchid.impl;

import com.eden.common.json.JSONElement;
import com.eden.orchid.Theme;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerators;
import com.eden.orchid.api.options.OrchidOptions;
import com.eden.orchid.api.registration.Contextual;
import com.eden.orchid.api.resources.OrchidResourceSource;
import com.eden.orchid.api.tasks.OrchidTasks;
import com.eden.orchid.utilities.OrchidUtils;
import com.sun.javadoc.RootDoc;
import lombok.Data;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

@Data
@Singleton
public final class Context implements Contextual, OrchidContext {

    private JSONObject root;
    private Theme theme;
    RootDoc rootDoc;

    private OrchidTasks orchidTasks;
    private OrchidOptions options;
    private OrchidGenerators generators;

    @Inject
    public Context(OrchidTasks orchidTasks, OrchidOptions options, OrchidGenerators generators) {
        this.orchidTasks = orchidTasks;
        this.options = options;
        this.generators = generators;
    }

    @Override public void bootstrap(Map<String, String[]> optionsMap, RootDoc rootDoc) {
        this.root = new JSONObject();
        this.rootDoc = rootDoc;

        root.put("options", new JSONObject());
        options.parseOptions(optionsMap, root.getJSONObject("options"));
    }

    @Override public boolean runTask(String taskName) {
        if (shouldContinue()) {
            reorderResourceSources();
            theme.onThemeSet();
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

    @Override public void build() {
        root.put("index", new JSONObject());
        generators.startIndexing(root.getJSONObject("index"));
        generators.startGeneration();
        theme.generateHomepage();
    }

    @Override public JSONElement query(String pointer) { return new JSONElement(root).query(pointer); }
    
    private void reorderResourceSources() {
        Theme theme = getContext().getTheme();

        reorderThemes();

        for(OrchidResourceSource source : OrchidUtils.resolveSet(OrchidResourceSource.class)) {
            if(source instanceof Theme) {
                if(!source.getClass().isAssignableFrom(theme.getClass())) {
                    source.setPriority(-1);
                }
            }
        }
    }

    private void reorderThemes() {
        Class<?> superclass = getContext().getTheme().getClass();
        int i = 0;

        // find the highest priority of any Theme
        int highestThemePriority = 0;
        for(OrchidResourceSource resourceSourceEntry : OrchidUtils.resolveSet(OrchidResourceSource.class)) {
            if (resourceSourceEntry instanceof Theme) {
                highestThemePriority = Math.max(highestThemePriority, resourceSourceEntry.getPriority());
            }
        }

        // Go through all Themes and set each parent theme as the next-highest Theme priority
        while(!superclass.equals(Theme.class)) {
            for(OrchidResourceSource resourceSourceEntry : OrchidUtils.resolveSet(OrchidResourceSource.class)) {
                if(resourceSourceEntry instanceof Theme) {
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
}
