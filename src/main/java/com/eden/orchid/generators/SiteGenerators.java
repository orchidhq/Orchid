package com.eden.orchid.generators;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.utilities.AutoRegister;
import com.eden.orchid.utilities.RegistrationProvider;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

@AutoRegister
public class SiteGenerators implements RegistrationProvider {
    public static Map<Integer, Generator> generators = new TreeMap<>(Collections.reverseOrder());
    private static JSONArray disabledGenerators;

    public static void startIndexing(JSONObject indexObject) {
        for (Map.Entry<Integer, Generator> generator : generators.entrySet()) {
            if(shouldUseGenerator(generator.getValue())) {
                Clog.d("Indexing generator: #{$1}:[#{$2 | className}]", generator.getKey(), generator.getValue());
                JSONElement element = generator.getValue().startIndexing();
                if (element != null) {
                    if (!EdenUtils.isEmpty(generator.getValue().getName())) {
                        indexObject.put(generator.getValue().getName(), element.getElement());
                    }
                }
            }
        }
    }

    public static void startGeneration() {
        for (Map.Entry<Integer, Generator> generator : generators.entrySet()) {
            if(shouldUseGenerator(generator.getValue())) {
                Clog.d("Using generator: #{$1}:[#{$2 | className}]", generator.getKey(), generator.getValue());
                generator.getValue().startGeneration();
            }
        }
    }

    public static boolean shouldUseGenerator(Generator generator) {
        if(disabledGenerators == null) {
            JSONElement el = Orchid.query("options.disabledGenerators");
            if(el != null) {
                if(el.getElement() instanceof JSONArray) {
                    disabledGenerators = (JSONArray) el.getElement();
                }
            }
        }

        if(disabledGenerators != null) {
            for(int i = 0; i < disabledGenerators.length(); i++) {
                if(disabledGenerators.getString(i).equalsIgnoreCase(generator.getClass().getName())) {
                    return false;
                }
                else if(disabledGenerators.getString(i).equalsIgnoreCase(generator.getClass().getSimpleName())) {
                    return false;
                }
            }
        }

        return true;
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
