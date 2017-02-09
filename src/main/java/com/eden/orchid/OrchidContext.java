package com.eden.orchid;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.generators.OrchidGenerators;
import com.eden.orchid.api.options.OrchidOptions;
import com.eden.orchid.api.registration.Contextual;
import com.eden.orchid.api.tasks.OrchidTasks;
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

    private OrchidTasks orchidTasks;
    private OrchidOptions options;
    private OrchidGenerators generators;

    public OrchidContext(Map<String, String[]> optionsMap) {
        this.root = new JSONObject();
        this.optionsMap = optionsMap;
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
            getRegistrar().reorderResourceSources();
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
}
