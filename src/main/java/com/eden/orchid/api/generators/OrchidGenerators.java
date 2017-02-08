package com.eden.orchid.api.generators;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.registration.AutoRegister;
import com.eden.orchid.api.registration.Contextual;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

@AutoRegister
public class OrchidGenerators implements Contextual {
    private JSONArray disabledGenerators;

    public void startIndexing(JSONObject indexObject) {
        for (Map.Entry<Integer, OrchidGenerator> generator : getRegistrar().resolveMap(OrchidGenerator.class).entrySet()) {
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

    public void startGeneration() {
        for (Map.Entry<Integer, OrchidGenerator> generator : getRegistrar().resolveMap(OrchidGenerator.class).entrySet()) {
            if(shouldUseGenerator(generator.getValue())) {
                Clog.d("Using generator: #{$1}:[#{$2 | className}]", generator.getKey(), generator.getValue());
                generator.getValue().startGeneration();
            }
        }
    }

    public boolean shouldUseGenerator(OrchidGenerator generator) {
        if(disabledGenerators == null) {
            JSONElement el = getContext().query("options.disabledGenerators");
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
}
