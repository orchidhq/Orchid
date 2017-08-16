package com.eden.orchid.api.tasks;

import com.eden.common.json.JSONElement;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.events.EventServiceImpl;
import com.eden.orchid.api.generators.OrchidGenerators;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.ObservableTreeSet;
import com.eden.orchid.utilities.OrchidUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

@Singleton
public final class TaskServiceImpl implements TaskService {

    public static String defaultTask = "build";

    private OrchidContext context;
    private Set<OrchidTask> tasks;
    private EventServiceImpl emitter;
    private OrchidGenerators generators;

    @Inject
    public TaskServiceImpl(Set<OrchidTask> tasks, EventServiceImpl emitter, OrchidGenerators generators) {
        this.tasks = new ObservableTreeSet<>(tasks);
        this.emitter = emitter;
        this.generators = generators;
    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

    public boolean run(String taskName) {
        OrchidTask foundTask = Arrays
                .stream((new String[]{taskName, defaultTask, "build"}))
                .map(t ->
                    tasks
                        .stream()
                        .filter(task -> task.getName().equals(t))
                        .findFirst()
                        .orElseGet(() -> null)
                )
                .filter(Objects::nonNull)
                .findFirst()
                .orElseGet(() -> null);

        if(foundTask != null) {
            emitter.broadcast(Orchid.Events.TASK_START, foundTask);
            foundTask.run();
            emitter.broadcast(Orchid.Events.TASK_FINISH, foundTask);
        }

        return true;
    }

    @Override
    public void build() {
        context.broadcast(Orchid.Events.BUILD_START);
        context.clearThemes();
        context.clearOptions();
        context.loadOptions();

        // Create theme menus
        JSONElement menuElement = context.query("menu");
        if (OrchidUtils.elementIsArray(menuElement)) {
            context.getTheme().createMenu(null, (JSONArray) menuElement.getElement());
        }
        else if (OrchidUtils.elementIsObject(menuElement)) {
            context.getTheme().createMenus((JSONObject) menuElement.getElement());
        }

        generators.startIndexing();

        // Add discovered assets
        // TODO: Leave this up to themes/pages/components to add their own assets rather than do it by force
        // TODO: Alternatively, allow this behavior as a flag?
        for (OrchidPage style : context.getIndex().find("assets/js")) {
            context.getTheme().addJs(style);
        }
        for (OrchidPage style : context.getIndex().find("assets/css")) {
            context.getTheme().addCss(style);
        }

        generators.startGeneration();
        context.broadcast(Orchid.Events.BUILD_FINISH);
    }

    @Override
    public void serve() {
        run("serve");
    }
}
