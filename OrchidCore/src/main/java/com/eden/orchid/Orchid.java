package com.eden.orchid;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OrchidFlag;
import com.eden.orchid.api.options.OrchidFlags;
import com.eden.orchid.api.registration.IgnoreModule;
import com.eden.orchid.api.theme.Theme;
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
    private static List<OrchidFlag> orchidFlags;
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

    public static List<OrchidFlag> findFlags() {
        if(Orchid.orchidFlags == null) {
            Orchid.orchidFlags = new ArrayList<>();

            FastClasspathScanner scanner = new FastClasspathScanner();
            scanner.matchClassesImplementing(OrchidFlag.class, (matchingClass) -> {
                try {
                    OrchidFlag option = matchingClass.newInstance();
                    if (option != null) {
                        Orchid.orchidFlags.add(option);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            });
            scanner.scan();
        }

        return Orchid.orchidFlags;
    }

    public static List<AbstractModule> findModules(Map<String, String[]> options) {
        if(Orchid.modules == null) {
            Orchid.modules = new ArrayList<>();

            FastClasspathScanner scanner = new FastClasspathScanner();
            scanner.matchSubclassesOf(OrchidModule.class, (matchingClass) -> {

                if(!matchingClass.isAnnotationPresent(IgnoreModule.class)) {
                    try {
                        AbstractModule provider = matchingClass.newInstance();
                        if (provider != null) {
                            Orchid.modules.add(provider);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            scanner.scan();
        }

        return Orchid.modules;
    }

    public static String findTask(Map<String, String[]> options) {
        return options.get("-task")[1];
    }

    public static Class<? extends Theme> findTheme(Map<String, String[]> options) throws ClassNotFoundException, ClassCastException {
        return (Class<? extends Theme>) Class.forName(options.get("-theme")[1]);
    }

// Make main Orchid object a singleton
//----------------------------------------------------------------------------------------------------------------------

    private OrchidContext context;
    private Injector injector;
    private Map<String, String[]> flags;

    public Orchid(Map<String, String[]> flags) {
        this.flags = flags;
    }

    public boolean start(List<AbstractModule> modules, Class<? extends Theme> themeClass, String task) {
        String moduleLog = "Using the following modules: ";
        moduleLog += "\n--------------------";
        for (AbstractModule module : modules) {
            if(!module.getClass().getName().startsWith("com.eden.orchid.OrchidModule")) {
                moduleLog += "\n * " + module.getClass().getName();
            }
        }
        moduleLog += "\n--------------------";
        Clog.i(moduleLog);

        modules.add(OrchidFlags.getInstance().parseFlags(this.flags));

        injector = Guice.createInjector(modules);
        context = injector.getInstance(OrchidContext.class);

        Theme theme = injector.getInstance(themeClass);
        context.setDefaultTheme(theme);
        boolean success = context.run(task);
        context.broadcast(Events.SHUTDOWN, success);
        return success;
    }

    public OrchidContext getContext() {
        return context;
    }

// Entry points, main routines
//----------------------------------------------------------------------------------------------------------------------

    public static void main(String[] args) {
        Map<String, String[]> options = Arrays
                .stream(args)
                .filter(s -> s.startsWith("-"))
                .map(s -> s.split("\\s+"))
                .collect(Collectors.toMap(s -> s[0], s -> s, (key1, key2) -> key1));

        try {
            List<AbstractModule> modules = Orchid.findModules(options);
            Class<? extends Theme> theme = Orchid.findTheme(options);
            String task = Orchid.findTask(options);

            boolean success;
            try {
                success = Orchid.getInstance(options).start(modules, theme, task);
            }
            catch (Exception e) {
                success = false;
                Clog.e("Something went wrong running Orchid: {}", e, e.getMessage());
            }
            System.exit(success ? 0 : 1);
        }
        catch (ClassNotFoundException e) {
            Clog.e("Theme class could not be found.");
            System.exit(1);
        }
        catch (ClassCastException e) {
            Clog.e("Class given for Theme is not a subclass of " + Theme.class.getName());
            System.exit(1);
        }
    }
}


// TODO HIGHEST PRIORITY - render everything against a specific theme, which is typically the context default theme, but may actually be different. Multiple themes FTW!