package com.eden.orchid.api.generators;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
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
import java.util.Objects;
import java.util.Set;

@Singleton
public final class OrchidGenerators {
    private JSONArray disabledGenerators;
    private Set<OrchidGenerator> generators;

    private OrchidContext context;
    private Map<String, List<OrchidPage>> indexPages;

    @Inject
    public OrchidGenerators(OrchidContext context, Set<OrchidGenerator> generators) {
        this.context = context;
        this.generators = new ObservableTreeSet<>(generators);
        this.indexPages = new HashMap<>();
    }

    public void startIndexing(JSONObject indexObject) {
        generators.stream()
                  .filter(this::shouldUseGenerator)
                  .map(this::indexGenerator)
                  .filter(Objects::nonNull)
                  .forEach(pair -> indexObject.put(pair.first, pair.second));
    }

    public void startGeneration() {
        generators.stream()
                  .filter(this::shouldUseGenerator)
                  .forEach(this::useGenerator);
    }

    private EdenPair<String, JSONObject> indexGenerator(OrchidGenerator generator) {
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
                    pageIndex.put("data", page.getData());

                    OrchidUtils.buildTaxonomy(page.getResource(), generatorIndex, pageIndex);

                    if(page.getResource() instanceof FreeableResource) {
                        ((FreeableResource) page.getResource()).free();
                    }
                }

                JSONObject fullIndexObject = generatorIndex.optJSONObject(generator.getName());
                if(fullIndexObject == null) {
                    fullIndexObject = generatorIndex;
                }

                return new EdenPair<>(generator.getName(), fullIndexObject);
            }
        }

        return null;
    }

    private void useGenerator(OrchidGenerator generator) {
        Clog.d("Using generator: #{$1}:[#{$2 | className}]", generator.getPriority(), generator);

        List<OrchidPage> generatorPages = null;

        if(!EdenUtils.isEmpty(generator.getName())) {
            generatorPages = indexPages.get(generator.getName());
        }

        if(generatorPages == null) {
            generatorPages = new ArrayList<>();
        }

        generator.startGeneration(generatorPages);
    }

    public boolean shouldUseGenerator(OrchidGenerator generator) {
        if(disabledGenerators == null) {
            JSONElement el = context.query("options.disabledGenerators");
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
