package com.eden.orchid.explorers.impl;

import com.eden.orchid.explorers.DocumentationExplorer;
import com.sun.javadoc.RootDoc;
import org.json.JSONArray;

public class PackageTreeExplorer implements DocumentationExplorer {

    @Override
    public String getKey() {
        return "packageTree";
    }

    public JSONArray startDiscovery(RootDoc root) {
        JSONArray jsonObject = new JSONArray();

        return jsonObject;
    }
}
