package com.eden.orchid.api.generators;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.indexing.OrchidIndex;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.resources.resource.FreeableResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.impl.indexing.OrchidInternalIndex;
import com.eden.orchid.utilities.ObservableTreeSet;
import com.eden.orchid.utilities.OrchidUtils;
import com.eden.orchid.utilities.PrioritizedSetFilter;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter @Setter
@Singleton
public final class OrchidGenerators {

    private Set<OrchidGenerator> allGenerators;
    private Set<OrchidGenerator> generators;
    private OrchidContext context;

    @Inject
    public OrchidGenerators(OrchidContext context, Set<OrchidGenerator> generators) {
        this.context = context;
        this.allGenerators = new ObservableTreeSet<>(generators);
    }

    public void startIndexing() {
        this.generators = new PrioritizedSetFilter<>(context, "generators", this.allGenerators).getFilteredSet();

        context.clearIndex();

        buildInternalIndex();
        buildExternalIndex();
        context.mergeIndices(context.getInternalIndex(), context.getExternalIndex());
    }

    public void startGeneration() {
        generators.stream()
                  .forEach(this::useGenerator);
    }

// Indexing phase
//----------------------------------------------------------------------------------------------------------------------

    private void buildInternalIndex() {
        generators.stream()
                  .forEach(this::indexGenerator);
    }

    private void indexGenerator(OrchidGenerator generator) {
        Clog.d("Indexing generator: #{$1}:[#{$2 | className}]", generator.getPriority(), generator);

        if(generator instanceof OptionsHolder) {
            JSONElement el = context.query("options." + generator.getKey());
            if(OrchidUtils.elementIsObject(el)) {
                ((OptionsHolder) generator).extractOptions(context, (JSONObject) el.getElement());
            }
            else {
                ((OptionsHolder) generator).extractOptions(context, new JSONObject());
            }
        }

        List<? extends OrchidPage> generatorPages = generator.startIndexing();

        if (!EdenUtils.isEmpty(generator.getKey()) && generatorPages != null && generatorPages.size() > 0) {
            OrchidInternalIndex index = new OrchidInternalIndex(generator.getKey());
            for(OrchidPage page : generatorPages) {
                index.addToIndex(generator.getKey() + "/" + page.getReference().getPath(), page);
                if(page.getResource() instanceof FreeableResource) {
                    ((FreeableResource) page.getResource()).free();
                }
            }
            context.addChildIndex(generator.getKey(), index);
        }
    }

    private void buildExternalIndex() {
        JSONElement externalIndexReferences = context.query("options.externalIndex");

        if(OrchidUtils.elementIsArray(externalIndexReferences)) {
            JSONArray externalIndex = (JSONArray) externalIndexReferences.getElement();

            for (int i = 0; i < externalIndex.length(); i++) {
                JSONObject indexJson = this.context.loadAdditionalFile(externalIndex.getString(i));
                if(indexJson != null) {
                    OrchidIndex index = OrchidIndex.fromJSON(context, indexJson);
                    context.addExternalChildIndex(index);
                }
            }
        }
    }

// Generation Phase
//----------------------------------------------------------------------------------------------------------------------

    private void useGenerator(OrchidGenerator generator) {
        Clog.d("Using generator: #{$1}:[#{$2 | className}]", generator.getPriority(), generator);

        List<? extends OrchidPage> generatorPages = null;
        if(!EdenUtils.isEmpty(generator.getKey())) {
            generatorPages = context.getGeneratorPages(generator.getKey());
        }
        if(generatorPages == null) {
            generatorPages = new ArrayList<>();
        }

        generator.startGeneration(generatorPages);
    }
}
