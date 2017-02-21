package com.eden.orchid;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.events.EventEmitter;
import com.eden.orchid.api.options.OrchidOption;
import com.eden.orchid.api.options.OrchidOptions;
import com.eden.orchid.api.tasks.OrchidTasks;
import com.eden.orchid.utilities.OrchidUtils;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.RootDoc;
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

// Doclet hackery to allow this to parse documentation as expected and not crash...
//----------------------------------------------------------------------------------------------------------------------

    /**
     * Get the number of arguments that a given option expects from the command line. This number includes the option
     * itself: for example '-d /output/javadoc' should return 2.
     *
     * @param option the option to parse
     * @return the number of arguments it expects from the command line
     */
    public static int optionLength(String option) {
        if(injector == null) {
            injector = Guice.createInjector(findModules());
        }

        return OrchidOptions.optionLength(OrchidUtils.resolveSet(injector, OrchidOption.class), option);
    }

    /**
     * NOTE: Without this method present and returning LanguageVersion.JAVA_1_5,
     * Javadoc will not process generics because it assumes LanguageVersion.JAVA_1_1
     *
     * @return language version (hard coded to LanguageVersion.JAVA_1_5)
     */
    public static LanguageVersion languageVersion() {
        return LanguageVersion.JAVA_1_5;
    }

// Data Members, Getters, Setters
//----------------------------------------------------------------------------------------------------------------------

    private static OrchidContext context;
    private static Injector injector;

    public static OrchidContext getContext() { return context; }
    public static Injector getInjector() { return injector; }

// Entry points, main routines
//----------------------------------------------------------------------------------------------------------------------

    public static void main(String[] args) {
        Map<String, String[]> options = Arrays.stream(args)
            .filter(s -> s.startsWith("-"))
            .map(s -> s.split("\\s+"))
            .collect(Collectors.toMap(s -> s[0], s -> s, (key1, key2) -> key1));

        String task = Arrays.stream(args)
            .filter(s -> !s.startsWith("-"))
            .findFirst()
            .orElseGet(() -> OrchidTasks.defaultTask);

        injector = Guice.createInjector(findModules());

        context = injector.getInstance(OrchidContext.class);
        EventEmitter emitter = injector.getInstance(EventEmitter.class);
        context.bootstrap(options, null);

        boolean success = context.runTask(task);

        emitter.broadcast(Events.SHUTDOWN, success);

        System.exit((success) ? 0 : 1);
    }

    public static boolean start(RootDoc rootDoc) {
        Map<String, String[]> options = Arrays.stream(rootDoc.options())
          .collect(Collectors.toMap(s -> s[0], s -> s, (key1, key2) -> key1));

        context = injector.getInstance(OrchidContext.class);
        EventEmitter emitter = injector.getInstance(EventEmitter.class);
        context.bootstrap(options, rootDoc);

        boolean success = context.runTask(OrchidTasks.defaultTask);

        emitter.broadcast(Events.SHUTDOWN, success);

        return success;
    }

    private static List<AbstractModule> findModules() {
        List<AbstractModule> modules = new ArrayList<>();

        FastClasspathScanner scanner = new FastClasspathScanner();
        scanner.matchSubclassesOf(OrchidModule.class, (matchingClass) -> {
            try {
                AbstractModule provider = matchingClass.newInstance();
                if (provider != null) {
                    Clog.i("Registering module of type '#{$1}'", new Object[]{matchingClass.getName()});
                    modules.add(provider);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        scanner.scan();

        return modules;
    }

// Events fired by Orchid Core
//----------------------------------------------------------------------------------------------------------------------
    public static class Events {
        public static final String INIT_COMPLETE      = "INIT_COMPLETE";
        public static final String OPTIONS_PARSED     = "OPTIONS_PARSED";
        public static final String BOOTSTRAP_COMPLETE = "BOOTSTRAP_COMPLETE";
        public static final String THEME_SET          = "THEME_SET";
        public static final String TASK_START         = "TASK_START";
        public static final String TASK_FINISH        = "TASK_FINISH";
        public static final String SHUTDOWN           = "SHUTDOWN";
    }
}
