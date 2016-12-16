package com.eden.orchid.explorers;

import com.sun.javadoc.RootDoc;
import org.json.JSONArray;

public interface DocumentationExplorer {
    String getKey();
    JSONArray startDiscovery(RootDoc root);
}
