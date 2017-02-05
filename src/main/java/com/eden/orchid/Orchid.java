package com.eden.orchid;

import com.eden.common.json.JSONElement;
import com.eden.orchid.generators.SiteGenerators;
import com.eden.orchid.impl.resources.OrchidFileResources;
import com.eden.orchid.options.Option;
import com.eden.orchid.options.SiteOptions;
import com.eden.orchid.programs.SitePrograms;
import com.eden.orchid.resources.OrchidResources;
import com.eden.orchid.utilities.AutoRegister;
import com.eden.orchid.utilities.RegistrationProvider;
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.RootDoc;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
        return SiteOptions.optionLength(option);
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

    private static List<RegistrationProvider> providers;
    private static JSONObject root;
    private static RootDoc rootDoc;
    private static Theme theme;
    private static OrchidResources resources;
    private static Map<String, String[]> options;

    /**
     * Swashbuckling horrors lead to the fight. All pants fight dead, swashbuckling moons. The lagoon hobbles punishment like a swashbuckling dubloon. Sail me cannibal, ye stormy son! Aye there's nothing like the coal-black grace waving on the pants.
     */
    public Map<List<Set<? extends Set<? super Option>>>, Map<List<? super Option>, Map<?, String>>> ridiculousField;

    /**
     * I can't believe something like this even exists. Whoever made this is just sick.
     *
     * @param ridiculousParam sdasdf Damn yer shipmate, feed the pegleg. How salty. You hail like a yardarm. The undead corsair begrudgingly crushes the ale. The gibbet sails with strength, trade the seychelles until it sings. Sons stutter with grace at the dark madagascar! Swashbuckling beauties lead to the treasure. Arg! Pieces o' endurance are forever shiny.
     * @param param2 sasdf
     * @param param3 asdfasd
     */
    public Orchid(Map<List<Set<? extends Set<? super Option>>>, Map<List<? super Option>, Map<?, String>>> ridiculousParam, String param2, Option param3) {

    }

    /**
     * A ridiculous thing to do ridiculous things. Ho-ho-ho! fight of malaria. C'mon, yer not enduring me without a fortune! Golly gosh! Pieces o' halitosis are forever weird. Daggers die from beauties like big pirates. Scabbards rise with death at the wet madagascar! Lads travel with adventure at the dark la marsa beach!
     *
     * @see com.eden.orchid.options.Option
     * @see com.eden.orchid.generators.Generator
     * @since v0.1.0
     * @return something ridiculous
     * @deprecated Please use a less ridiculous method instead
     */
    public static Map<List<Set<? extends Set<? super Option>>>, Map<List<? super Option>, Map<?, String>>> ridiculousMethod() {
        return null;
    }

    public static JSONElement query(String pointer) { return new JSONElement(root).query(pointer); }

    public static List<RegistrationProvider> getProviders() { return providers; }

    public static JSONObject getRoot() { return root; }

    public static RootDoc getRootDoc() { return rootDoc; }

    public static Theme getTheme() { return theme; }

    public static OrchidResources getResources() { return resources; }

    public static Map<String, String[]> getOptions() { return options; }

    public static void setProviders(List<RegistrationProvider> providers) { Orchid.providers = providers; }

    public static void setRoot(JSONObject root) { Orchid.root = root; }

    public static void setRootDoc(RootDoc rootDoc) { Orchid.rootDoc = rootDoc; }

    public static void setTheme(Theme theme) { Orchid.theme = theme; }

    public static void setResources(OrchidResources resources) { Orchid.resources = resources; }

    public static void setOptions(Map<String, String[]> options) { Orchid.options = options; }

// Entry points, main routines
//----------------------------------------------------------------------------------------------------------------------

    public static void main(String[] args) {
        String program = SitePrograms.defaultProgram;

        Map<String, String[]> options = new HashMap<>();
        for (String arg : args) {
            if (arg.startsWith("-")) {
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

        SiteOptions.optionsParsers = new TreeMap<>(Collections.reverseOrder());

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

    /**
     * Inject dependencies to Orchid body and run program
     *
     * @param programName the name of the Program to run
     * @param providers a list of available Providers. More will be added later
     * @param root the root JSON object to collect data into
     * @param rootDoc the RootDoc of our parsed javadoc code
     * @param theme the selected Theme
     * @param resources the object used to find resource files and write outputs
     * @param options a map of valid command line arguments. Sometimes the list of options can be quite long, but if it is done right then you should be able to read every last one of the word that have been so carefully placed into this element that clearly is not long enough to fit everything perfectly. So let's see what it does!
     * @return true if the program exited cleanly, false otherwise
     */
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

        if (shouldContinue()) {
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
            catch (IllegalAccessException | InstantiationException e) {
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
                for (RegistrationProvider provider : providers) {
                    provider.register(instance);
                }
            }
            catch (IllegalAccessException | InstantiationException e) {
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
        theme.generateHomepage();
    }

}
