package com.eden.orchid.generators;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.OrchidUtils;
import com.sun.javadoc.RootDoc;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

public class SiteGenerators {
    public static Map<Integer, Generator> generators = new TreeMap<>();

    public static void startDiscovery(RootDoc root, JSONObject generatorsObject) {
        for(Map.Entry<Integer, Generator> generator : generators.entrySet()) {
            Clog.d("Using generator: #{$1}:[#{$2 | className}]", generator.getKey(), generator.getValue());

            JSONObject generatorData = new JSONObject();

            generator.getValue().startDiscovery(root, generatorData);

            if(!OrchidUtils.isEmpty(generator.getValue().getName())) {
                generatorsObject.put(generator.getValue().getName(), generatorData);
            }
        }
    }
}
