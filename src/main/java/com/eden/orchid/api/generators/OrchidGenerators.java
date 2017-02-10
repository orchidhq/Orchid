package com.eden.orchid.api.generators;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.registration.Contextual;
import com.eden.orchid.utilities.AlwaysSortedTreeSet;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

@Singleton
public class OrchidGenerators implements Contextual {
    private JSONArray disabledGenerators;
    private Set<OrchidGenerator> generators;

    @Inject
    public OrchidGenerators(Set<OrchidGenerator> generators) {
        this.generators = new AlwaysSortedTreeSet<>(generators);
    }

    public void startIndexing(JSONObject indexObject) {
        for (OrchidGenerator generator : generators) {
            if(shouldUseGenerator(generator)) {
                Clog.d("Indexing generator: #{$1}:[#{$2 | className}]", generator.priority(), generator);
                JSONElement element = generator.startIndexing();
                if (element != null) {
                    if (!EdenUtils.isEmpty(generator.getName())) {
                        indexObject.put(generator.getName(), element.getElement());
                    }
                }
            }
        }
    }

    public void startGeneration() {
        for (OrchidGenerator generator : generators) {
            if(shouldUseGenerator(generator)) {
                Clog.d("Using generator: #{$1}:[#{$2 | className}]", generator.priority(), generator);
                generator.startGeneration();
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
