package com.eden.orchid.explorers;

import com.sun.javadoc.RootDoc;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class DocumentationExploration {

    public static Set<DocumentationExplorer> explorers = new HashSet<>();

    public static JSONObject docs;

    public static JSONObject startDiscovery(RootDoc root) {
        if(docs == null) {
            docs = new JSONObject();
            initializeDocumentationExplorer(root);
        }

        return docs;
    }

    private static void initializeDocumentationExplorer(RootDoc root) {
        for(DocumentationExplorer explorer : explorers) {
            docs.put(explorer.getKey(), explorer.startDiscovery(root));
        }
    }
}
