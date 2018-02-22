package com.eden.orchid;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidSecurityManager;
import com.eden.orchid.api.events.OrchidEvent;
import com.eden.orchid.api.options.OrchidFlags;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This is the main entry point to the Orchid build process. It does little more than create a OrchidContextImpl for Orchid to runTask
 * within, and then set that context into motion. It is the single point-of-entry for starting the Orchid process; both
 * Javadoc's `start` method and the Java `main` method are in here, which create the appropriate OrchidContext and then
 * runTask a single Orchid task.
 */
public final class Orchid {

// Make main Orchid object a singleton
//----------------------------------------------------------------------------------------------------------------------

    private static Orchid instance;

    @Getter private OrchidContext context;
    @Getter private Injector injector;

    public static Orchid getInstance() {
        if (instance == null) {
            instance = new Orchid();
        }

        return instance;
    }

    private Orchid() { }

// Entry points, main routines
//----------------------------------------------------------------------------------------------------------------------

    public static void main(String[] args) {
        Map<String, String[]> options = Arrays
                .stream(args)
                .filter(s -> s.startsWith("-"))
                .map(s -> s.split("\\s+"))
                .collect(Collectors.toMap(s -> s[0], s -> s, (key1, key2) -> key1));

        boolean success = Orchid.getInstance().start(
                StandardModule.builder().flags(options).build()
        );
        System.exit((success) ? 0 : 1);
    }

    public boolean start(Module... modules) {
        List<Module> modulesList = new ArrayList<>();
        Collections.addAll(modulesList, modules);
        return start(modulesList);
    }

    public boolean start(List<Module> modules) {
        try {
            String moduleLog = "Using the following modules: ";
            moduleLog += "\n--------------------";
            for (Module module : modules) {
                if (!module.getClass().getName().startsWith("com.eden.orchid.OrchidModule")) {
                    moduleLog += "\n * " + module.getClass().getName();
                }
            }
            moduleLog += "\n--------------------";
            Clog.d(moduleLog);

            injector = Guice.createInjector(modules);

            String flagLog = "Flag values: ";
            flagLog += "\n--------------------\n";
            flagLog += OrchidFlags.getInstance().printFlags();
            flagLog += "--------------------";
            Clog.d(flagLog);

            context = injector.getInstance(OrchidContext.class);

            try {
                OrchidSecurityManager manager = injector.getInstance(OrchidSecurityManager.class);
                System.setSecurityManager(manager);
            }
            catch (Exception e) {

            }

            Clog.i("Running Orchid version {}, site version {} in {} environment", context.getOrchidVersion(), context.getVersion(), context.getEnvironment());
            context.start();
            context.finish();
            return true;
        }
        catch (Exception e) {
            Clog.e("Something went wrong running Orchid: {}", e, e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

// Events fired by Orchid Core
//----------------------------------------------------------------------------------------------------------------------

    public static class Lifecycle {
        public static class InitComplete extends OrchidEvent {
            private InitComplete(Object sender) { super(sender); }

            public static InitComplete fire(Object sender) { return new InitComplete(sender); }
        }

        public static class OnStart extends OrchidEvent {
            private OnStart(Object sender) { super(sender); }

            public static OnStart fire(Object sender) { return new OnStart(sender); }
        }

        public static class OnFinish extends OrchidEvent {
            private OnFinish(Object sender) { super(sender); }

            public static OnFinish fire(Object sender) { return new OnFinish(sender); }
        }

        public static class Shutdown extends OrchidEvent {
            private Shutdown(Object sender) { super(sender); }

            public static Shutdown fire(Object sender) { return new Shutdown(sender); }
        }

        public static class TaskStart extends OrchidEvent {
            private TaskStart(Object sender) { super(sender); }

            public static TaskStart fire(Object sender) { return new TaskStart(sender); }
        }

        public static class BuildStart extends OrchidEvent {
            private BuildStart(Object sender) { super(sender); }

            public static BuildStart fire(Object sender) { return new BuildStart(sender); }
        }

        public static class ProgressEvent extends OrchidEvent {
            protected String progressType;
            protected int currentProgress;
            protected int maxProgress;

            private ProgressEvent(Object sender, String progressType, int currentProgress, int maxProgress) {
                super(progressType, sender);
                this.progressType = progressType;
                this.currentProgress = currentProgress;
                this.maxProgress = maxProgress;
            }

            @Override
            public String toString() {
                return Clog.format("{}/{}", currentProgress, maxProgress);
            }
        }

        public static class IndexProgress extends ProgressEvent {
            private IndexProgress(Object sender, int currentProgress, int maxProgress) {
                super(sender, "indexprogress", currentProgress, maxProgress);
            }

            public static IndexProgress fire(Object sender, int currentProgress, int maxProgress) { return new IndexProgress(sender, currentProgress, maxProgress); }
        }

        public static class BuildProgress extends ProgressEvent {
            protected long millis;

            private BuildProgress(Object sender, int currentProgress, int maxProgress, long millis) {
                super(sender, "buildprogress", currentProgress, maxProgress);
                this.millis = millis;
            }

            @Override
            public String toString() {
                return Clog.format("{}/{}/{}", currentProgress, maxProgress, millis);
            }

            public static BuildProgress fire(Object sender, int currentProgress, int maxProgress, long millis) { return new BuildProgress(sender, currentProgress, maxProgress, millis); }
        }

        public static class BuildFinish extends OrchidEvent {
            private BuildFinish(Object sender) { super(sender); }

            public static BuildFinish fire(Object sender) { return new BuildFinish(sender); }
        }

        public static class TaskFinish extends OrchidEvent {
            private TaskFinish(Object sender) { super(sender); }

            public static TaskFinish fire(Object sender) { return new TaskFinish(sender); }
        }

        public static class FilesChanged extends OrchidEvent {
            private FilesChanged(Object sender) { super(sender); }

            public static FilesChanged fire(Object sender) { return new FilesChanged(sender); }
        }

        public static class ClearCache extends OrchidEvent {
            private ClearCache(Object sender) { super(sender); }

            public static ClearCache fire(Object sender) { return new ClearCache(sender); }
        }

        public static class EndSession extends OrchidEvent {
            private EndSession(Object sender) { super(sender); }

            public static EndSession fire(Object sender) { return new EndSession(sender); }
        }
    }

}
