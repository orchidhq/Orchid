package com.eden.orchid.compilers;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.Orchid;
import com.eden.orchid.OrchidUtils;
import com.eden.orchid.compilers.impl.LiquidCompiler;
import com.eden.orchid.compilers.impl.MarkdownCompiler;
import com.sun.javadoc.RootDoc;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarFile;

public class SiteResources {

    public static Set<AssetCompiler> assetCompilers = new HashSet<>();
    public static ContentCompiler contentCompiler;
    public static PageCompiler pageCompiler;

    private static JSONObject siteResources;

    public static JSONObject startDiscovery(RootDoc root) {
        if(siteResources == null) {
            siteResources = new JSONObject();
            initializeResources(root);
        }

        return siteResources;
    }

    private static void initializeResources(RootDoc root) {
        // Compile resources internal to the JAR, such as the standard templates and styles
        JarFile orchidJar = OrchidUtils.jarForClass(Orchid.class, null);
        if (orchidJar != null) {
            for(AssetCompiler compiler : assetCompilers) {
                try {
                    addFilesToArray(siteResources, compiler.getKey(), OrchidUtils.copyAndCompileResourcesToDirectory(orchidJar, compiler));
                }
                catch (IOException e) {
                    Clog.e("Something went wrong compiling '#{$1}' resources", new Object[] {compiler.getKey()});
                }
            }
        }

        // Compile resources that are passed to the doclet, such as style customizations and static pages
        for(AssetCompiler compiler : assetCompilers) {
            try {
                addFilesToArray(siteResources, compiler.getKey(), OrchidUtils.copyAndCompileExternalResourcesToDirectory(root, compiler));
            }
            catch (IOException e) {
                Clog.e("Something went wrong compiling '#{$1}' resources", new Object[] {compiler.getKey()});
            }
        }
    }

    private static void addFilesToArray(JSONObject srcObject, String key, JSONArray arrayToAdd) {
        if(!srcObject.has(key)) {
            srcObject.put(key, new JSONArray());
        }

        for(int i = 0; i < arrayToAdd.length(); i++) {
            srcObject.getJSONArray(key).put(arrayToAdd.get(i));
        }
    }

    public static String compile(String extension, String source, JSONObject json) {
        switch(extension.toLowerCase()) {
            case "markdown":
            case "md":
                return new MarkdownCompiler().compile(source, json);
            case "htm":
            case "html":
            case "liquid":
                return new LiquidCompiler().compile(source, json);
            default:
                return source;
        }
    }
}
