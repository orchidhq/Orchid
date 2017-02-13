package com.eden.orchid;

import com.eden.orchid.api.OrchidContext;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the main entry point to the Orchid build process. It does little more than create a Context for Orchid to runTask
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
        String task = OrchidTasks.defaultTask;

        Map<String, String[]> options = new HashMap<>();
        for (String arg : args) {
            if (arg.startsWith("-")) {
                String[] argPieces = arg.split("\\s+");
                options.put(argPieces[0], argPieces);
            }
            else {
                task = arg;
            }
        }

        injector = Guice.createInjector(findModules());

        context = injector.getInstance(OrchidContext.class);
        context.bootstrap(options, null);

        boolean success = context.runTask(task);

        System.exit((success) ? 0 : 1);
    }

    public static boolean start(RootDoc rootDoc) {
        Map<String, String[]> options = new HashMap<>();
        for (String[] a : rootDoc.options()) {
            options.put(a[0], a);
        }

        context = injector.getInstance(OrchidContext.class);
        context.bootstrap(options, rootDoc);

        return context.runTask(OrchidTasks.defaultTask);
    }

    private static List<AbstractModule> findModules() {
        List<AbstractModule> modules = new ArrayList<>();

        FastClasspathScanner scanner = new FastClasspathScanner();
        scanner.matchSubclassesOf(OrchidModule.class, (matchingClass) -> {
            try {
                AbstractModule provider = matchingClass.newInstance();
                if (provider != null) {
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
}
