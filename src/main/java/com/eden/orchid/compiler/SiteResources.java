package com.eden.orchid.compiler;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.JarUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.compiler.impl.CompileCss;
import com.eden.orchid.compiler.impl.CompileJs;
import com.eden.orchid.compiler.impl.CompilePages;
import com.sun.javadoc.RootDoc;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarFile;

public class SiteResources {

    public static Set<AssetCompiler> compilers = new HashSet<>();

    private static JSONObject siteResources;

    public static JSONObject startDiscovery(RootDoc root) {
        if(siteResources == null) {
            siteResources = new JSONObject();
            initializeResources(root);
        }

        return siteResources;
    }

    private static void initializeResources(RootDoc root) {
        compilers.add(new CompileCss());
        compilers.add(new CompileJs());
        compilers.add(new CompilePages());

        // Compile resources internal to the JAR, such as the standard templates and styles
        JarFile orchidJar = JarUtils.jarForClass(Orchid.class, null);
        if (orchidJar != null) {
            for(AssetCompiler compiler : compilers) {
                try {
                    addFilesToArray(siteResources, compiler.getKey(), JarUtils.copyAndCompileResourcesToDirectory(orchidJar, compiler));
                }
                catch (IOException e) {
                    Clog.e("Something went wrong compiling '#{$1}' resources", new Object[] {compiler.getKey()});
                }
            }
        }

        // Compile resources that are passed to the doclet, such as style customizations and static pages
        for(AssetCompiler compiler : compilers) {
            try {
                addFilesToArray(siteResources, compiler.getKey(), JarUtils.copyAndCompileExternalResourcesToDirectory(root, compiler));
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
}
