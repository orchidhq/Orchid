package com.eden.orchid.discover;

import com.sun.javadoc.RootDoc;
import org.json.JSONObject;

public class DiscoverAll {
    public static JSONObject startDiscovery(RootDoc root) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("packageTree",   DiscoverPackageTree.startDiscovery(root));
        jsonObject.put("packages",      DiscoverPackages.startDiscovery(root));
        jsonObject.put("classes",       DiscoverClasses.startDiscovery(root));

        return jsonObject;
    }
}
