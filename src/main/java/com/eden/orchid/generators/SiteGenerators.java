package com.eden.orchid.generators;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.JSONElement;
import com.eden.orchid.Orchid;
import com.eden.orchid.OrchidUtils;
import com.sun.javadoc.RootDoc;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class SiteGenerators {
    public static Map<Integer, Generator> generators = new TreeMap<>(Collections.reverseOrder());

    public static void startDiscovery(RootDoc root, JSONObject generatorsObject) {

        int maxIterations = (int) Orchid.query("options.maxIterations").getElement();

        int iteration = 1;

        while(true) {
            int generatorsFinished = 0;

            for(Map.Entry<Integer, Generator> generator : generators.entrySet()) {
                Clog.d("Iteration: #{$1} - Using generator: #{$2}:[#{$3 | className}]", iteration, generator.getKey(), generator.getValue());

                JSONElement element = generator.getValue().startDiscovery(root, iteration);

                if(element != null) {
                    if(!OrchidUtils.isEmpty(generator.getValue().getName())) {
                        generatorsObject.put(generator.getValue().getName(), element.getElement());
                    }

                    generatorsFinished++;
                }
            }

            if(generatorsFinished == generators.size() || iteration >= maxIterations) {
                break;
            }
            else {
                iteration++;
            }
        }
    }
}
