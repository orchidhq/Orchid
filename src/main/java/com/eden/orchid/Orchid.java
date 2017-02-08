package com.eden.orchid;

import com.eden.orchid.api.options.OrchidOptions;
import com.eden.orchid.api.registration.OrchidRegistrar;
import com.eden.orchid.api.tasks.OrchidTasks;
import com.eden.orchid.impl.registration.Registrar;
import com.eden.orchid.impl.resources.OrchidFileResources;
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.RootDoc;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the main entry point to the Orchid build process. It does little more than create a Context for Orchid to run
 * within, and then set that context into motion. It is the single point-of-entry for starting the Orchid process; both
 * Javadoc's `start` method and the Java `main` method are in here, which create the appropriate OrchidContext and then
 * run a single Orchid task.
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
        if(earlyUseOptions == null) {
            registrar = new Registrar();
            registrar.addToResolver(new OrchidFileResources());
            earlyUseOptions = registrar.resolve(OrchidOptions.class);
        }

        return earlyUseOptions.optionLength(option);
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
    private static OrchidRegistrar registrar;
    private static OrchidOptions earlyUseOptions;

    public static OrchidContext getContext() { return context; }

    public static OrchidRegistrar getRegistrar() {
        return registrar;
    }

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

        registrar = new Registrar();
        registrar.addToResolver(new OrchidFileResources());

        context = new OrchidContext(options);

        boolean success = context.run(task);

        System.exit((success) ? 0 : 1);
    }

    public static boolean start(RootDoc rootDoc) {
        Map<String, String[]> options = new HashMap<>();
        for (String[] a : rootDoc.options()) {
            options.put(a[0], a);
        }

        // registrar was already created during 'optionLength' phase
        registrar.addToResolver(rootDoc);

        context = new OrchidContext(options);

        return context.run(OrchidTasks.defaultTask);
    }
}
