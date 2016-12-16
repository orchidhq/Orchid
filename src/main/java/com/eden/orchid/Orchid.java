package com.eden.orchid;

import com.eden.orchid.compiler.AssetCompiler;
import com.eden.orchid.compiler.SiteResources;
import com.eden.orchid.explorers.DocumentationExploration;
import com.eden.orchid.explorers.DocumentationExplorer;
import com.eden.orchid.options.SiteOption;
import com.eden.orchid.options.SiteOptions;
import com.sun.javadoc.RootDoc;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import liqp.Template;
import liqp.filters.Filter;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Orchid {
    public static int optionLength(String option) {
        return SiteOptions.optionLength(option);
    }

    public static JSONObject all;

    public static boolean start(RootDoc root) {
        // Scan the classpath and register all available plugins
        pluginScan();

        // Discover all resources, options, and documentation data, and anything else so it can be rendered
        discoveryScan(root);

        // Render site pages
        generationScan();

        return true;
    }

    private static void pluginScan() {
        FastClasspathScanner scanner = new FastClasspathScanner();
        scanner.matchClassesWithAnnotation(AutoRegister.class, (matchingClass) -> {
            try {
                Object instance = matchingClass.newInstance();

                if(instance instanceof SiteOption) {
                    SiteOptions.optionsParsers.add((SiteOption) instance);
                }
                else if(instance instanceof DocumentationExplorer) {
                    DocumentationExploration.explorers.add((DocumentationExplorer) instance);
                }
                else if(instance instanceof AssetCompiler) {
                    SiteResources.compilers.add((AssetCompiler) instance);
                }
                else if(instance instanceof Filter) {
                    Filter.registerFilter((Filter) instance);
                }
            }
            catch (IllegalAccessException|InstantiationException e) {
                e.printStackTrace();
            }
        });
        scanner.scan();
    }

    private static void discoveryScan(RootDoc root) {
        // These exploration methods should be made into pluggable explorers
        all = new JSONObject();
        all.put("site", SiteOptions.startDiscovery(root));
        all.put("res",  SiteResources.startDiscovery(root));
        all.put("docs", DocumentationExploration.startDiscovery(root));

        // Make all site data available via a single Liquid object
        all.put("root", new JSONObject(all.toString()));
    }

    private static void generationScan() {
        // These generate methods should be made into pluggable generators
        generateIndex();
        generateClasses();
        generatePackages();
        generatePages();
        generatePosts();
    }

    private static void generateIndex() {
        Path file = Paths.get(SiteOptions.outputDir + "/index.html");
        try {
            Template template = Template.parse(JarUtils.getResource("assets/html/index.html"));
            Files.write(file, template.render(all.toString(2)).getBytes());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateClasses() {

    }

    private static void generatePackages() {

    }

    private static void generatePages() {

    }

    private static void generatePosts() {

    }
}
