package com.eden.orchid.generators;

import com.sun.javadoc.RootDoc;
import org.json.JSONArray;
import org.json.JSONObject;

public interface Generator {
    JSONArray startDiscovery(RootDoc root, JSONObject jsonObject);
    String getKey();
    int priority();
}
