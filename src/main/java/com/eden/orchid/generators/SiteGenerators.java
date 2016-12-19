package com.eden.orchid.generators;

import com.caseyjbrooks.clog.Clog;
import com.sun.javadoc.RootDoc;
import com.sun.tools.javac.resources.compiler;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

public class SiteGenerators {
    public static Map<Integer, Generator> generators = new TreeMap<>();

    public static void startDiscovery(RootDoc root, JSONObject generatorsObject) {
        for(Map.Entry<Integer, Generator> generator : generators.entrySet()) {
            Clog.d("Using generator: #{$1}:[#{$2 | className}]", generator.getKey(), generator.getValue());

            generator.getValue().startDiscovery(root, generatorsObject);
        }
    }
}
