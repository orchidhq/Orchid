package com.eden.orchid.api.tasks;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.events.On;
import com.eden.orchid.api.events.OrchidEvent;
import com.eden.orchid.api.events.OrchidEventListener;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.IntDefault;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.server.FileWatcher;
import com.eden.orchid.api.server.OrchidServer;
import com.google.inject.name.Named;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @since v1.0.0
 * @orchidApi services
 */
@Singleton
public final class TaskServiceImpl implements TaskService, OrchidEventListener {

    private OrchidContext context;
    private final Set<OrchidTask> tasks;
    private final Set<OrchidCommand> commands;

    private final OrchidServer server;
    private final FileWatcher watcher;

    private final String task;
    private final String resourcesDir;

    private long lastBuild;
    private boolean isBuilding;

    @Getter @Setter
    @Option @IntDefault(5)
    @Description("The minimum time, in seconds, to wait in between builds.")
    private int watchDebounceTimeout;

    @Inject
    public TaskServiceImpl(
            Set<OrchidTask> tasks,
            Set<OrchidCommand> commands,
            @Named("task") String task,
            @Named("resourcesDir") String resourcesDir,
            OrchidServer server,
            FileWatcher watcher) {
        this.tasks = new TreeSet<>(tasks);
        this.commands = new TreeSet<>(commands);

        this.server = server;
        this.watcher = watcher;

        this.task = task;
        this.resourcesDir = resourcesDir;

        this.lastBuild = 0;
        this.isBuilding = false;
    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

    @Override
    public void onPostStart() {
        runTask(task);
    }

    @Override
    public boolean runTask(String taskName) {
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
    public boolean runCommand(String commandName, String parameters) {
        OrchidCommand foundCommand = commands
                .stream()
                .sorted()
                .filter(command -> command.matches(commandName))
                .findFirst()
                .orElse(null);

        if (foundCommand != null) {
            OrchidCommand freshCommand = context.getInjector().getInstance(foundCommand.getClass());

            String[] pieces = parameters.split("\\s+");
            String[] paramKeys = foundCommand.parameters();

            Map<String, String> paramMap = new HashMap<>();

            int i = 0;
            while (i < paramKeys.length && i < pieces.length) {
                paramMap.put(paramKeys[i], pieces[i]);
                i++;
            }

            JSONObject paramsJSON = new JSONObject(paramMap);

            freshCommand.extractOptions(context, paramsJSON);

            try {
                freshCommand.run(commandName);
                return true;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Clog.e("Could not find matching command for {}", commandName);
        }

        return false;
    }

    @Override
    public void build() {
        if (!isBuilding) {
            long secondsSinceLastBuild = (System.currentTimeMillis() - lastBuild)/1000;

            if(secondsSinceLastBuild > watchDebounceTimeout) {
                isBuilding = true;
                context.broadcast(Orchid.Lifecycle.BuildStart.fire(this));
                Clog.i("Build Starting...");

                context.clearOptions();
                context.broadcast(Orchid.Lifecycle.ClearCache.fire(this));
                context.loadOptions();

                context.clearThemes();
                context.pushTheme(context.getDefaultTheme());

                context.extractServiceOptions();

                context.startIndexing();
                context.startGeneration();

                context.broadcast(Orchid.Lifecycle.BuildFinish.fire(this));

                lastBuild = System.currentTimeMillis();
                isBuilding = false;

                Clog.i("Build Complete");
            }
        }
        else {
            Clog.e("Build already in progress, skipping.");
        }
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
        if (server != null && server.getWebsocket() != null) {
            server.getWebsocket().sendMessage("Files Changed", "");
        }
        context.build();
    }

    @On(Orchid.Lifecycle.EndSession.class)
    public void onEndSession(Orchid.Lifecycle.EndSession event) {
        if (server != null && server.getWebsocket() != null) {
            server.getWebsocket().sendMessage("Ending Session", "");
        }
        context.broadcast(Orchid.Lifecycle.Shutdown.fire(this));
        System.exit(0);
    }

    @On(value = OrchidEvent.class, subclasses = true)
    public void onAnyEvent(OrchidEvent event) {
        if (server != null && server.getWebsocket() != null) {
            server.getWebsocket().sendMessage(event.getType(), event.toString());
        }
    }
}
