package com.eden.orchid.generators;

import com.sun.javadoc.RootDoc;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

public class SiteGenerators {
    public static Map<Integer, Generator> generators = new TreeMap<>();

    public static void startDiscovery(RootDoc root, JSONObject generatorsObject) {
        for(Map.Entry<Integer, Generator> compiler : generators.entrySet()) {
            compiler.getValue().startDiscovery(root, generatorsObject);
        }
    }
}
