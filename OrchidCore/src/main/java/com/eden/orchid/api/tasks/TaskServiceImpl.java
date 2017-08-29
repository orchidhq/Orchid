package com.eden.orchid.api.tasks;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.events.On;
import com.eden.orchid.api.generators.OrchidGenerators;
import com.eden.orchid.api.server.FileWatcher;
import com.eden.orchid.api.server.OrchidServer;
import com.google.inject.name.Named;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.EventListener;
import java.util.Set;
import java.util.TreeSet;

@Singleton
public final class TaskServiceImpl implements TaskService, EventListener {

    public static String defaultTask = "build";

    private OrchidContext context;
    private Set<OrchidTask> tasks;
    private OrchidGenerators generators;

    private OrchidServer server;
    private FileWatcher watcher;

    private final String task;
    private final String resourcesDir;

    @Inject
    public TaskServiceImpl(
            Set<OrchidTask> tasks,
            OrchidGenerators generators,
            @Named("task") String task,
            @Named("resourcesDir") String resourcesDir,
            OrchidServer server,
            FileWatcher watcher) {
        this.tasks = new TreeSet<>(tasks);
        this.generators = generators;

        this.server = server;
        this.watcher = watcher;

        this.task = task;
        this.resourcesDir = resourcesDir;
    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

    @Override
    public void onStart() {
        run(task);
    }

    public boolean run(String taskName) {
        OrchidTask foundTask = tasks
                .stream()
                .sorted()
                .filter(task -> task.getName().equals(taskName))
                .findFirst()
                .orElse(null);

        if (foundTask != null) {
            context.broadcast(Orchid.Events.TASK_START, foundTask);
            foundTask.run();
            context.broadcast(Orchid.Events.TASK_FINISH, foundTask);
            return true;
        }
        else {
            Clog.e("Could not find task {} to run", taskName);
            return false;
        }
    }

    @Override
    public void build() {
        context.broadcast(Orchid.Events.BUILD_START);
        context.clearThemes();
        context.clearOptions();
        context.loadOptions();

        context.pushTheme(context.getDefaultTheme());

        generators.startIndexing();
        generators.startGeneration();

        context.broadcast(Orchid.Events.BUILD_FINISH);
    }

    @Override
    public void watch() {
        watcher.startWatching(resourcesDir);
    }

    @Override
    public void serve() {
        try {
            server.start(8080);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

// Build Events
//----------------------------------------------------------------------------------------------------------------------

    @On(Orchid.Events.FILES_CHANGED)
    public void onFilesChanges() {
        server.getWebsocket().sendMessage("Files Changed");
        context.build();
    }

    @On(Orchid.Events.FORCE_REBUILD)
    public void onForceRebuild() {
        server.getWebsocket().sendMessage("Forcing Rebuild");
        context.build();
    }

    @On(Orchid.Events.BUILD_START)
    public void onBuildStarted() {
        if (server != null && server.getWebsocket() != null) {
            server.getWebsocket().sendMessage("Rebuilding site...");
        }
    }

    @On(Orchid.Events.BUILD_FINISH)
    public void onBuildFinished() {
        if (server != null && server.getWebsocket() != null) {
            server.getWebsocket().sendMessage("Site Rebuilt");
        }
    }

    @On(Orchid.Events.END_SESSION)
    public void onEndSession() {
        server.getWebsocket().sendMessage("Ending Session");
        context.broadcast(Orchid.Events.SHUTDOWN);
        System.exit(0);
    }

    @On()
    public void onAnyEvent(String event) {
        if (server != null && server.getWebsocket() != null) {
            server.getWebsocket().sendMessage("Event: " + event);
        }
    }
}
