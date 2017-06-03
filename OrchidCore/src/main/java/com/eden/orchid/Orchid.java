package com.eden.orchid;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.events.EventService;
import com.eden.orchid.api.options.OrchidOption;
import com.eden.orchid.api.tasks.OrchidTasks;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

import java.util.ArrayList;
import java.util.Arrays;
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

// Events fired by Orchid Core
//----------------------------------------------------------------------------------------------------------------------

    public static class Events {
        public static final String INIT_COMPLETE = "INIT_COMPLETE";
        public static final String OPTIONS_PARSED = "OPTIONS_PARSED";
        public static final String DATAFILES_PARSED = "DATAFILES_PARSED";
        public static final String BOOTSTRAP_COMPLETE = "BOOTSTRAP_COMPLETE";
        public static final String THEME_SET = "THEME_SET";
        public static final String TASK_START = "TASK_START";
        public static final String TASK_FINISH = "TASK_FINISH";
        public static final String BUILD_START = "BUILD_START";
        public static final String BUILD_FINISH = "BUILD_FINISH";
        public static final String SHUTDOWN = "SHUTDOWN";
        public static final String FILES_CHANGED = "FILES_CHANGED";
        public static final String FORCE_REBUILD = "FORCE_REBUILD";
        public static final String END_SESSION = "END_SESSION";
    }

// Make main Orchid object a singleton
//----------------------------------------------------------------------------------------------------------------------

    private static Orchid instance;
    private static List<OrchidOption> orchidOptions;
    private static List<AbstractModule> modules;

    public static Orchid getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Orchid has not been initialized yet");
        }

        return instance;
    }

    public static Orchid getInstance(Map<String, String[]> options) {
        if (instance == null) {
            instance = new Orchid(options);
        }
        return getInstance();
    }

    public static List<AbstractModule> findModules(Map<String, String[]> options) {
        if(Orchid.modules == null) {
            Orchid.modules = new ArrayList<>();

            FastClasspathScanner scanner = new FastClasspathScanner();
            scanner.matchSubclassesOf(OrchidModule.class, (matchingClass) -> {
                try {
                    AbstractModule provider = matchingClass.newInstance();
                    if (provider != null) {
                        Orchid.modules.add(provider);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            });
            scanner.scan();
        }

        return Orchid.modules;
    }

    public static List<OrchidOption> findOptions() {
        if(Orchid.orchidOptions == null) {
            Orchid.orchidOptions = new ArrayList<>();

            FastClasspathScanner scanner = new FastClasspathScanner();
            scanner.matchSubclassesOf(OrchidOption.class, (matchingClass) -> {
                try {
                    OrchidOption option = matchingClass.newInstance();
                    if (option != null) {
                        Orchid.orchidOptions.add(option);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            });
            scanner.scan();
        }

        return Orchid.orchidOptions;
    }

// Make main Orchid object a singleton
//----------------------------------------------------------------------------------------------------------------------

    private OrchidContext context;
    private Injector injector;
    private Map<String, String[]> options;

    public Orchid(Map<String, String[]> options) {
        this.options = options;
    }

    public boolean start(List<AbstractModule> modules, String task) {
        for (AbstractModule module : modules) {
            Clog.i("Registering module of type '#{$1}'", new Object[]{module.getClass().getName()});
        }

        injector = Guice.createInjector(modules);

        context = injector.getInstance(OrchidContext.class);
        EventService emitter = injector.getInstance(EventService.class);
        context.bootstrap(options);

        boolean success = context.runTask(task);

        emitter.broadcast(Events.SHUTDOWN, success);

        return success;
    }

    public OrchidContext getContext() {
        return context;
    }

    public Injector getInjector() {
        return injector;
    }

    // Entry points, main routines
//----------------------------------------------------------------------------------------------------------------------

    public static void main(String[] args) {
        Map<String, String[]> options = Arrays
                .stream(args)
                .filter(s -> s.startsWith("-"))
                .map(s -> s.split("\\s+"))
                .collect(Collectors.toMap(s -> s[0], s -> s, (key1, key2) -> key1));

        String task = Arrays
                .stream(args)
                .filter(s -> !s.startsWith("-"))
                .findFirst()
                .orElse(OrchidTasks.defaultTask);

        System.exit((Orchid.getInstance(options).start(findModules(options), task)) ? 0 : 1);
    }
}
