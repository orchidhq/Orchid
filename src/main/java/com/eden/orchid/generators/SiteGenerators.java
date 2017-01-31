package com.eden.orchid.generators;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.utilities.RegistrationProvider;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class SiteGenerators implements RegistrationProvider {
    public static Map<Integer, Generator> generators = new TreeMap<>(Collections.reverseOrder());

    public static void startIndexing(JSONObject indexObject) {
        for (Map.Entry<Integer, Generator> generator : generators.entrySet()) {
            Clog.d("Indexing generator: #{$1}:[#{$2 | className}]", generator.getKey(), generator.getValue());

            JSONElement element = generator.getValue().startIndexing();

            if (element != null) {
                if (!EdenUtils.isEmpty(generator.getValue().getName())) {
                    indexObject.put(generator.getValue().getName(), element.getElement());
                }
            }
        }
    }

    public static void startGeneration() {
        for (Map.Entry<Integer, Generator> generator : generators.entrySet()) {
            Clog.d("Using generator: #{$1}:[#{$2 | className}]", generator.getKey(), generator.getValue());

            generator.getValue().startGeneration();
        }
    }

    @Override
    public void register(Object object) {
        if (object instanceof Generator) {
            Generator generator = (Generator) object;
            int priority = generator.priority();
            while (generators.containsKey(priority)) {
                priority--;
            }

            SiteGenerators.generators.put(priority, generator);
        }
    }
}
