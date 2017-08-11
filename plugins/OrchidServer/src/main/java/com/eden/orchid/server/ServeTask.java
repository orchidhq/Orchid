package com.eden.orchid.server;

import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.events.On;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.server.api.FileWatcher;
import com.google.inject.name.Named;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.EventListener;

@Singleton
public class ServeTask extends OrchidTask implements EventListener {

    private OrchidContext context;
    private OrchidServer server;
    private FileWatcher watcher;

    private final String resourcesDir;

    @Inject
    public ServeTask(
            OrchidContext context,
            @Named("resourcesDir") String resourcesDir,
            OrchidServer server,
            FileWatcher watcher) {
        this.context = context;
        this.server = server;
        this.watcher = watcher;

        this.resourcesDir = resourcesDir;
    }

    @Override
    public String getName() {
        return "serve";
    }

    @Override
    public String getDescription() {
        return "Makes it easier to create content for your Orchid site by watching your resources for changes and " +
                "rebuilding the site on any changes. A static HTTP server is also created in the root of your site and " +
                "the baseUrl set to this server's address so you can preview the output.";
    }

    @Override
    public void run() {
        File file = new File(resourcesDir);

        if (file.exists() && file.isDirectory()) {
            JSONObject rootJson = context.getRoot();
//            JSONObject optionsJson = rootJson.getJSONObject("options");

            context.build();
            try {
                server.start(8080);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            //TODO: find out how I could dynamically set the base URL (i'm thinking this should not be a flag)
//            optionsJson.put("baseUrl", "http://localhost:" + server.getHttpServerPort());

            watcher.startWatching(resourcesDir);
        }
    }

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

