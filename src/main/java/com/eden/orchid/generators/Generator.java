package com.eden.orchid.generators;

import com.sun.javadoc.RootDoc;
import org.json.JSONObject;

public interface Generator {
    void startDiscovery(RootDoc root, JSONObject jsonObject);
    int priority();
    String getName();
}
