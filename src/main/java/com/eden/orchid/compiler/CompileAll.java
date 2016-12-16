package com.eden.orchid.compiler;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.JarUtils;
import com.eden.orchid.Orchid;
import com.sun.javadoc.RootDoc;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarFile;

public class CompileAll {
    public static Set<AssetCompiler> compilers = new HashSet<>();

    static {
        CompileAll.compilers.add(new CompileCss());
        CompileAll.compilers.add(new CompileJs());
        CompileAll.compilers.add(new CompilePages());
    }

    public static JSONObject compileAll(RootDoc root) {
        JSONObject jsonObject = new JSONObject();

        // Compile resources internal to the JAR, such as the standard templates and styles
        JarFile orchidJar = JarUtils.jarForClass(Orchid.class, null);
        if (orchidJar != null) {
            for(AssetCompiler compiler : compilers) {
                try {
                    addFilesToArray(jsonObject, compiler.getKey(), JarUtils.copyAndCompileResourcesToDirectory(orchidJar, compiler));
                }
                catch (IOException e) {
                    Clog.e("Something went wrong compiling '#{$1}' resources", new Object[] {compiler.getKey()});
                }
            }
        }

        // Compile resources that are passed to the doclet, such as style customizations and static pages
        for(AssetCompiler compiler : compilers) {
            try {
                addFilesToArray(jsonObject, compiler.getKey(), JarUtils.copyAndCompileExternalResourcesToDirectory(root, compiler));
            }
            catch (IOException e) {
                Clog.e("Something went wrong compiling '#{$1}' resources", new Object[] {compiler.getKey()});
            }
        }

        return jsonObject;

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
