package com.eden.orchid.api.tasks;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.events.On;
import com.eden.orchid.api.events.OrchidEvent;
import com.eden.orchid.api.events.OrchidEventListener;
import com.eden.orchid.api.server.FileWatcher;
import com.eden.orchid.api.server.OrchidServer;
import com.google.inject.name.Named;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

@Singleton
public final class TaskServiceImpl implements TaskService, OrchidEventListener {

    private OrchidContext context;
    private final Set<OrchidTask> tasks;

    private final OrchidServer server;
    private final FileWatcher watcher;

    private final String task;
    private final String resourcesDir;

    @Inject
    public TaskServiceImpl(
            Set<OrchidTask> tasks,
            @Named("task") String task,
            @Named("resourcesDir") String resourcesDir,
            OrchidServer server,
            FileWatcher watcher) {
        this.tasks = new TreeSet<>(tasks);

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
    public void onPostStart() {
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
            context.broadcast(Orchid.Lifecycle.TaskStart.fire(this));
            foundTask.run();
            context.broadcast(Orchid.Lifecycle.TaskFinish.fire(this));
            return true;
        }
        else {
            Clog.e("Could not find task {} to run", taskName);
            return false;
        }
    }

    @Override
    public void build() {
        context.broadcast(Orchid.Lifecycle.BuildStart.fire(this));
        context.clearThemes();
        context.clearOptions();
        context.loadOptions();

        context.pushTheme(context.getDefaultTheme());

        context.startIndexing();
        context.startGeneration();

        context.broadcast(Orchid.Lifecycle.BuildFinish.fire(this));
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

    @On(Orchid.Lifecycle.FilesChanged.class)
    public void onFilesChanges(Orchid.Lifecycle.FilesChanged event) {
        server.getWebsocket().sendMessage("Files Changed");
        context.build();
    }

    @On(Orchid.Lifecycle.EndSession.class)
    public void onEndSession(Orchid.Lifecycle.EndSession event) {
        server.getWebsocket().sendMessage("Ending Session");
        context.broadcast(Orchid.Lifecycle.Shutdown.fire(this));
        System.exit(0);
    }

    @On(value = OrchidEvent.class, subclasses = true)
    public void onAnyEvent(OrchidEvent event) {
        if (server != null && server.getWebsocket() != null) {
            server.getWebsocket().sendMessage(event.toString());
        }
    }
}
