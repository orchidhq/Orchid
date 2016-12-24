package com.eden.orchid.generators;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.JSONElement;
import com.eden.orchid.OrchidUtils;
import com.sun.javadoc.RootDoc;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class SiteGenerators {
    public static Map<Integer, Generator> generators = new TreeMap<>(Collections.reverseOrder());

    public static void startIndexing(RootDoc root, JSONObject indexObject) {
        for(Map.Entry<Integer, Generator> generator : generators.entrySet()) {
            Clog.d("Indexing generator: #{$2}:[#{$3 | className}]", generator.getKey(), generator.getValue());

            JSONElement element = generator.getValue().startIndexing(root);

            if(element != null) {
                if(!OrchidUtils.isEmpty(generator.getValue().getName())) {
                    indexObject.put(generator.getValue().getName(), element.getElement());
                }
            }
        }
    }

    public static void startGeneration(RootDoc root, JSONObject generatorsObject) {
        for(Map.Entry<Integer, Generator> generator : generators.entrySet()) {
            Clog.d("Using generator: #{$2}:[#{$3 | className}]", generator.getKey(), generator.getValue());

            JSONElement element = generator.getValue().startGeneration(root);

            if(element != null) {
                if(!OrchidUtils.isEmpty(generator.getValue().getName())) {
                    generatorsObject.put(generator.getValue().getName(), element.getElement());
                }
            }
        }
    }
}
