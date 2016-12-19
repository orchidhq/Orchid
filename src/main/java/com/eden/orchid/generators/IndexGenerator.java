package com.eden.orchid.generators;

import com.sun.javadoc.RootDoc;
import org.json.JSONArray;
import org.json.JSONObject;

public interface IndexGenerator extends Generator {
    JSONArray startDiscovery(RootDoc root, JSONObject jsonObject);
    String getKey();
    int priority();
}
