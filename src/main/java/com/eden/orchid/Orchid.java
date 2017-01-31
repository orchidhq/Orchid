package com.eden.orchid;

import com.eden.common.json.JSONElement;
import com.eden.orchid.generators.SiteGenerators;
import com.eden.orchid.options.SiteOptions;
import com.eden.orchid.programs.SitePrograms;
import com.eden.orchid.resources.OrchidResources;
import com.eden.orchid.impl.resources.OrchidFileResources;
import com.eden.orchid.utilities.AutoRegister;
import com.eden.orchid.utilities.RegistrationProvider;
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.RootDoc;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Orchid {

// Doclet hackery to allow this to parse documentation as expected and not crash...
//----------------------------------------------------------------------------------------------------------------------

    /**
     * Get the number of arguments that a given option expects from the command line. This number includes the option
     * itself: for example '-d /output/javadoc' should return 2.
     *
     * @param option  the option to parse
     * @return  the number of arguments it expects from the command line
     */
    public static int optionLength(String option) {
        return SiteOptions.optionLength(option);
    }

    /**
     * NOTE: Without this method present and returning LanguageVersion.JAVA_1_5,
     *       Javadoc will not process generics because it assumes LanguageVersion.JAVA_1_1
     * @return language version (hard coded to LanguageVersion.JAVA_1_5)
     */
    public static LanguageVersion languageVersion() {
        return LanguageVersion.JAVA_1_5;
    }

// Data Members, Getters, Setters
//----------------------------------------------------------------------------------------------------------------------

    private static List<RegistrationProvider> providers;
    private static JSONObject root;
    private static RootDoc rootDoc;
    private static Theme theme;
    private static OrchidResources resources;
    private static Map<String, String[]> options;

    public static JSONElement query(String pointer) { return new JSONElement(root).query(pointer); }

    public static List<RegistrationProvider> getProviders() { return providers; }
    public static JSONObject getRoot()                      { return root;      }
    public static RootDoc getRootDoc()                      { return rootDoc;   }
    public static Theme getTheme()                          { return theme;     }
    public static OrchidResources getResources()            { return resources; }
    public static Map<String, String[]> getOptions()        { return options;   }

    public static void setProviders(List<RegistrationProvider> providers) { Orchid.providers = providers; }
    public static void setRoot(JSONObject root)                           { Orchid.root = root;           }
    public static void setRootDoc(RootDoc rootDoc)                        { Orchid.rootDoc = rootDoc;     }
    public static void setTheme(Theme theme)                              { Orchid.theme = theme;         }
    public static void setResources(OrchidResources resources)            { Orchid.resources = resources; }
    public static void setOptions(Map<String, String[]> options)          { Orchid.options = options;     }

// Entry points, main routines
//----------------------------------------------------------------------------------------------------------------------

    public static void main(String[] args) {
        String program = SitePrograms.defaultProgram;

        Map<String, String[]> options = new HashMap<>();
        for(String arg : args) {
            if(arg.startsWith("-")) {
                String[] argPieces = arg.split("\\s+");
                options.put(argPieces[0], argPieces);
            }
            else {
                program = arg;
            }
        }

        boolean success = runOrchid(
                program,
                new ArrayList<>(),
                new JSONObject(),
                null,
                null,
                new OrchidFileResources(),
                options);

        System.exit((success) ? 0 : 1);
    }

    public static boolean start(RootDoc rootDoc) {
        Map<String, String[]> options = new HashMap<>();
        for (String[] a : rootDoc.options()) {
            options.put(a[0], a);
        }

        return runOrchid(
                SitePrograms.defaultProgram,
                new ArrayList<>(),
                new JSONObject(),
                rootDoc,
                null,
                new OrchidFileResources(),
                options);
    }

// Internal routines orchestrated by main routines
//----------------------------------------------------------------------------------------------------------------------

    // inject dependencies and run Orchid
    public static boolean runOrchid(
            String programName,
            List<RegistrationProvider> providers,
            JSONObject root,
            RootDoc rootDoc,
            Theme theme,
            OrchidResources resources,
            Map<String, String[]> options) {

        Orchid.providers = providers;
        Orchid.root = root;
        Orchid.rootDoc = rootDoc;
        Orchid.theme = theme;
        Orchid.resources = resources;
        Orchid.options = options;

        bootstrap();

        if(shouldContinue()) {
            SitePrograms.runProgram(programName);
            return true;
        }
        else {
            return false;
        }
    }

    public static void bootstrap() {
        providerScan();
        pluginScan();
        optionsScan();
    }

    public static void build() {
        indexingScan();
        generationScan();
        generateHomepage();
    }

    private static void providerScan() {
        FastClasspathScanner scanner = new FastClasspathScanner();
        scanner.matchClassesImplementing(RegistrationProvider.class, (matchingClass) -> {
            try {
                RegistrationProvider instance = matchingClass.newInstance();
                providers.add(instance);
            }
            catch (IllegalAccessException|InstantiationException e) {
                e.printStackTrace();
            }
        });
        scanner.scan();
    }

    private static void pluginScan() {
        FastClasspathScanner scanner = new FastClasspathScanner();
        scanner.matchClassesWithAnnotation(AutoRegister.class, (matchingClass) -> {
            try {
                Object instance = matchingClass.newInstance();
                for(RegistrationProvider provider : providers) {
                    provider.register(instance);
                }
            }
            catch (IllegalAccessException|InstantiationException e) {
                e.printStackTrace();
            }
        });
        scanner.scan();
    }

    private static void optionsScan() {
        root.put("options", new JSONObject());
        SiteOptions.parseOptions(options, root.getJSONObject("options"));
    }

    private static boolean shouldContinue() {
        return SiteOptions.shouldContinue() && (theme != null) && theme.shouldContinue();
    }

    private static void indexingScan() {
        root.put("index", new JSONObject());
        SiteGenerators.startIndexing(root.getJSONObject("index"));
    }

    private static void generationScan() {
        SiteGenerators.startGeneration();
    }

    private static void generateHomepage() {
        theme.generateHomepage(root);
    }

}
