package com.eden.orchid;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.compilers.AssetCompiler;
import com.eden.orchid.compilers.Compiler;
import com.eden.orchid.compilers.ContentCompiler;
import com.eden.orchid.compilers.PageCompiler;
import com.eden.orchid.compilers.SiteResources;
import com.eden.orchid.explorers.DocumentationExploration;
import com.eden.orchid.explorers.DocumentationExplorer;
import com.eden.orchid.options.SiteOption;
import com.eden.orchid.options.SiteOptions;
import com.sun.javadoc.RootDoc;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.json.JSONObject;

import java.io.File;
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
                Clog.i("Found annotated class: #{$1}", new Object[] {matchingClass.getName()});

                Object instance = matchingClass.newInstance();

                // Register all compilers
                if(instance instanceof Compiler) {
                    Compiler compiler = (Compiler) instance;

                    // Register as a generic compiler
                    SiteResources.compilers.put(compiler.priority(), compiler);

                    // Register also as a specialized compiler
                    if(instance instanceof AssetCompiler) {
                        SiteResources.assetCompilers.add((AssetCompiler) instance);
                    }
                    else if(instance instanceof ContentCompiler) {
                        if(SiteResources.contentCompiler == null || compiler.priority() > SiteResources.contentCompiler.priority()) {
                            SiteResources.contentCompiler = (ContentCompiler) compiler;
                        }
                    }
                    else if(instance instanceof PageCompiler) {
                        if(SiteResources.pageCompiler == null || compiler.priority() > SiteResources.pageCompiler.priority()) {
                            SiteResources.pageCompiler = (PageCompiler) compiler;
                        }
                    }
                }

                // Register command-line options
                if(instance instanceof SiteOption) {
                    SiteOptions.optionsParsers.add((SiteOption) instance);
                }

                // Register documentation explorers
                else if(instance instanceof DocumentationExplorer) {
                    DocumentationExploration.explorers.add((DocumentationExplorer) instance);
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
            String compiledContent = SiteResources.pageCompiler.compile("html", OrchidUtils.getResourceFileContents("assets/layouts/index.html"), all);
            Files.write(file, compiledContent.getBytes());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateClasses() {
        try {
            String template = OrchidUtils.getResourceFileContents("assets/layouts/classDoc.html");

            for(int i = 0; i < all.getJSONObject("docs").getJSONArray("classes").length(); i++) {
                JSONObject classInfo = new JSONObject(all.getJSONObject("docs").getJSONArray("classes").getJSONObject(i).toString());
                JSONObject classData = new JSONObject(all.toString());
                classData.put("classDoc", classInfo);

                String outputPath = SiteOptions.outputDir + File.separator + "classes";
                File outputFile = new File(outputPath);
                Path classesFile = Paths.get(outputPath + File.separator + classInfo.getString("simpleName") + ".html");
                if (!outputFile.exists()) {
                    outputFile.mkdirs();
                }

                String compiledContent = SiteResources.pageCompiler.compile("html", template, classData);

                Files.write(classesFile, compiledContent.getBytes());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generatePackages() {
        try {
            JSONObject packageData = new JSONObject(all.toString());
            String template = OrchidUtils.getResourceFileContents("assets/layouts/packageDoc.html");

            for(int i = 0; i < all.getJSONObject("docs").getJSONArray("packages").length(); i++) {
                JSONObject packageInfo = all.getJSONObject("docs").getJSONArray("packages").getJSONObject(i);
                packageData.put("packageDoc", packageInfo);

                String outputPath = SiteOptions.outputDir + File.separator + "packages" + File.separator + packageInfo.getString("name").replace(".", File.separator);
                File outputFile = new File(outputPath);
                if (!outputFile.exists()) {
                    outputFile.mkdirs();
                }

                Path classesFile = Paths.get(outputPath + ".html");
                String compiledContent = SiteResources.pageCompiler.compile("html", template, packageData);
                Files.write(classesFile, compiledContent.getBytes());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generatePages() {

    }

    private static void generatePosts() {

    }
}
