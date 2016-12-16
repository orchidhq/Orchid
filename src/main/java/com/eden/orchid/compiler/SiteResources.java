package com.eden.orchid.compiler;

import com.sun.javadoc.RootDoc;
import org.json.JSONObject;

public class SiteResources {
    public static JSONObject startDiscovery(RootDoc root) {
        return CompileAll.compileAll(root);
    }
}
