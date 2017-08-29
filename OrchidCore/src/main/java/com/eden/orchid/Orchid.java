package com.eden.orchid;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OrchidFlag;
import com.eden.orchid.api.options.OrchidFlags;
import com.eden.orchid.api.registration.IgnoreModule;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

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
        // Overall lifecycle
        public static final String INIT_COMPLETE = "INIT_COMPLETE";
        public static final String ON_START = "ON_START";
        public static final String ON_FINISH = "ON_FINISH";
        public static final String SHUTDOWN = "SHUTDOWN";

        // Build Lifecycle
        public static final String TASK_START = "TASK_START";
        public static final String BUILD_START = "BUILD_START";
        public static final String BUILD_FINISH = "BUILD_FINISH";
        public static final String TASK_FINISH = "TASK_FINISH";

        // Server Events
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

// Make main Orchid object a singleton
//----------------------------------------------------------------------------------------------------------------------

    private OrchidContext context;
    private Injector injector;
    private Map<String, String[]> flags;

    public Orchid(Map<String, String[]> flags) {
        this.flags = flags;
    }

    public void start(List<AbstractModule> modules) {
        modules.add(OrchidFlags.getInstance().parseFlags(this.flags));
        injector = Guice.createInjector(modules);

        String moduleLog = "Using the following modules: ";
        moduleLog += "\n--------------------";
        for (AbstractModule module : modules) {
            if(!module.getClass().getName().startsWith("com.eden.orchid.OrchidModule")) {
                moduleLog += "\n * " + module.getClass().getName();
            }
        }
        moduleLog += "\n--------------------";
        Clog.i(moduleLog);

        DumperOptions options = new DumperOptions();
        options.setIndent(4);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Yaml yaml = new Yaml(options);

        String flagLog = "Flag values: ";
        flagLog += "\n--------------------\n";
        flagLog += yaml.dump(OrchidFlags.getInstance().getData().toMap());
        flagLog += "--------------------";
        Clog.i(flagLog);

        context = injector.getInstance(OrchidContext.class);
        context.start();
        context.finish();
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

        List<AbstractModule> modules = Orchid.findModules(options);

        try {
            Orchid.getInstance(options).start(modules);
            System.exit(0);
        }
        catch (Exception e) {
            Clog.e("Something went wrong running Orchid: {}", e, e.getMessage());
            System.exit(1);
        }
    }
}
