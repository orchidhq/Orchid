package com.eden.orchid.api.generators;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.registration.Contextual;
import com.eden.orchid.api.resources.OrchidPage;
import com.eden.orchid.api.resources.resource.FreeableResource;
import com.eden.orchid.utilities.ObservableTreeSet;
import com.eden.orchid.utilities.OrchidUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Singleton
public final class OrchidGenerators implements Contextual {
    private JSONArray disabledGenerators;
    private Set<OrchidGenerator> generators;

    private Map<String, List<OrchidPage>> indexPages;

    @Inject
    public OrchidGenerators(Set<OrchidGenerator> generators) {
        this.generators = new ObservableTreeSet<>(generators);
        this.indexPages = new HashMap<>();
    }

    public void startIndexing(JSONObject indexObject) {
        for (OrchidGenerator generator : generators) {
            if(shouldUseGenerator(generator)) {
                Clog.d("Indexing generator: #{$1}:[#{$2 | className}]", generator.getPriority(), generator);

                List<OrchidPage> generatorPages = generator.startIndexing();
                if (generatorPages != null && generatorPages.size() > 0) {
                    if (!EdenUtils.isEmpty(generator.getName())) {
                        indexPages.put(generator.getName(), generatorPages);

                        JSONObject generatorIndex = new JSONObject();

                        for(OrchidPage page : generatorPages) {
                            JSONObject pageIndex = new JSONObject();

                            pageIndex.put("name", page.getReference().getTitle());
                            pageIndex.put("url", page.getReference().toString());

                            OrchidUtils.buildTaxonomy(page.getResource(), generatorIndex, pageIndex);

                            if(page.getResource() instanceof FreeableResource) {
                                ((FreeableResource) page.getResource()).free();
                            }
                        }

                        JSONObject fullIndexObject = generatorIndex.optJSONObject(generator.getName());
                        if(fullIndexObject == null) {
                            fullIndexObject = generatorIndex;
                        }

                        indexObject.put(generator.getName(), fullIndexObject);
                    }
                }
            }
        }
    }

    public void startGeneration() {
        for (OrchidGenerator generator : generators) {
            if(shouldUseGenerator(generator)) {
                Clog.d("Using generator: #{$1}:[#{$2 | className}]", generator.getPriority(), generator);

                List<OrchidPage> generatorPages = null;
                if (!EdenUtils.isEmpty(generator.getName())) {
                    generatorPages = indexPages.get(generator.getName());
                }

                if(generatorPages == null) {
                    generatorPages = new ArrayList<>();
                }

                generator.startGeneration(generatorPages);
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
