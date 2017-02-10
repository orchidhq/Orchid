package com.eden.orchid;

import com.eden.common.json.JSONElement;
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
public final class OrchidContext implements Contextual {

    private JSONObject root;
    private Theme theme;
    private Map<String, String[]> optionsMap;
    RootDoc rootDoc;

    private OrchidTasks orchidTasks;
    private OrchidOptions options;
    private OrchidGenerators generators;

    public OrchidContext(Map<String, String[]> optionsMap, RootDoc rootDoc) {
        this.root = new JSONObject();
        this.optionsMap = optionsMap;
        this.rootDoc = rootDoc;
    }

    @Inject
    public void setOrchidTasks(OrchidTasks orchidTasks) {
        this.orchidTasks = orchidTasks;
    }

    @Inject
    public void setOptions(OrchidOptions options) {
        this.options = options;
    }

    @Inject
    public void setGenerators(OrchidGenerators generators) {
        this.generators = generators;
    }

    public boolean run(String taskName) {
        optionsScan();

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

    private void optionsScan() {
        root.put("options", new JSONObject());
        options.parseOptions(optionsMap, root.getJSONObject("options"));
    }

    private boolean shouldContinue() {
        return options.shouldContinue() && (theme != null) && theme.shouldContinue();
    }

    private void indexingScan() {
        root.put("index", new JSONObject());
        generators.startIndexing(root.getJSONObject("index"));
    }

    private void generationScan() {
        generators.startGeneration();
    }

    public JSONElement query(String pointer) { return new JSONElement(root).query(pointer); }

    public void build() {
        indexingScan();
        generationScan();
        theme.generateHomepage();
    }
















    private void reorderResourceSources() {
        Theme theme = Orchid.getContext().getTheme();

        reorderThemes();

        for(OrchidResourceSource source : OrchidUtils.resolveSet(OrchidResourceSource.class)) {
            if(source instanceof Theme) {
                if(!source.getClass().isAssignableFrom(theme.getClass())) {
                    source.setResourcePriority(-1);
                }
            }
        }
    }

    private void reorderThemes() {
        Class<?> superclass = Orchid.getContext().getTheme().getClass();
        int i = 0;

        // find the highest priority of any Theme
        int highestThemePriority = 0;
        for(OrchidResourceSource resourceSourceEntry : OrchidUtils.resolveSet(OrchidResourceSource.class)) {
            if (resourceSourceEntry instanceof Theme) {
                highestThemePriority = Math.max(highestThemePriority, resourceSourceEntry.getResourcePriority());
            }
        }

        // Go through all Themes and set each parent theme as the next-highest Theme priority
        while(!superclass.equals(Theme.class)) {
            for(OrchidResourceSource resourceSourceEntry : OrchidUtils.resolveSet(OrchidResourceSource.class)) {
                if(resourceSourceEntry instanceof Theme) {
                    Theme theme = (Theme) resourceSourceEntry;
                    if (theme.getClass().equals(superclass)) {
                        theme.setResourcePriority((highestThemePriority) - i);
                        break;
                    }
                }
            }

            i++;
            superclass = superclass.getSuperclass();
        }
    }
}
