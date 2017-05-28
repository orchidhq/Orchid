package com.eden.orchid.api.generators;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.indexing.OrchidIndex;
import com.eden.orchid.api.resources.resource.FreeableResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.impl.indexing.OrchidCompositeIndex;
import com.eden.orchid.impl.indexing.OrchidInternalIndex;
import com.eden.orchid.impl.indexing.OrchidRootExternalIndex;
import com.eden.orchid.impl.indexing.OrchidRootInternalIndex;
import com.eden.orchid.utilities.FileLoader;
import com.eden.orchid.utilities.ObservableTreeSet;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Singleton
public final class OrchidGenerators {
    private JSONArray disabledGenerators;
    private Set<OrchidGenerator> generators;
    private OrchidContext context;
    private FileLoader fileLoader;

    private OrchidRootInternalIndex internalIndex;
    private OrchidRootExternalIndex externalIndex;
    private OrchidCompositeIndex compositeIndex;

    @Inject
    public OrchidGenerators(OrchidContext context, Set<OrchidGenerator> generators, FileLoader fileLoader) {
        this.context = context;
        this.generators = new ObservableTreeSet<>(generators);
        this.fileLoader = fileLoader;
    }

    public void startIndexing() {
        buildInternalIndex();
        buildExternalIndex();
        mergeIndices(this.internalIndex, this.externalIndex);
    }

    public void startGeneration() {
        generators.stream()
                  .filter(this::shouldUseGenerator)
                  .forEach(this::useGenerator);
    }

// Indexing phase
//----------------------------------------------------------------------------------------------------------------------

    private void buildInternalIndex() {
        this.internalIndex = new OrchidRootInternalIndex();
        generators.stream()
                  .filter(this::shouldUseGenerator)
                  .forEach(this::indexGenerator);
    }

    private void indexGenerator(OrchidGenerator generator) {
        Clog.d("Indexing generator: #{$1}:[#{$2 | className}]", generator.getPriority(), generator);
        List<? extends OrchidPage> generatorPages = generator.startIndexing();

        if (!EdenUtils.isEmpty(generator.getName()) && generatorPages != null && generatorPages.size() > 0) {
            OrchidInternalIndex index = new OrchidInternalIndex(generator.getName());
            for(OrchidPage page : generatorPages) {
                index.addToIndex(generator.getName() + "/" + page.getReference().getFullPath(), page);
                if(page.getResource() instanceof FreeableResource) {
                    ((FreeableResource) page.getResource()).free();
                }
            }
            this.internalIndex.addChildIndex(generator.getName(), index);
        }
    }

    private void buildExternalIndex() {
        this.externalIndex = new OrchidRootExternalIndex();

        JSONElement externalIndexReferences = context.query("options.data.externalIndex");

        if(OrchidUtils.elementIsArray(externalIndexReferences)) {
            JSONArray externalIndex = (JSONArray) externalIndexReferences.getElement();

            for (int i = 0; i < externalIndex.length(); i++) {
                JSONObject indexJson = this.fileLoader.loadAdditionalFile(externalIndex.getString(i));
                if(indexJson != null) {
                    OrchidIndex index = OrchidIndex.fromJSON(context, indexJson);
                    this.externalIndex.addChildIndex(index);
                }
            }
        }
    }

    private void mergeIndices(OrchidIndex... indices) {
        this.compositeIndex = new OrchidCompositeIndex("composite");
        for(OrchidIndex index : indices) {
            if(index != null) {
                this.compositeIndex.mergeIndex(index);
            }
        }
    }

// Generation Phase
//----------------------------------------------------------------------------------------------------------------------

    private void useGenerator(OrchidGenerator generator) {
        Clog.d("Using generator: #{$1}:[#{$2 | className}]", generator.getPriority(), generator);

        List<? extends OrchidPage> generatorPages = null;
        if(!EdenUtils.isEmpty(generator.getName())) {
            generatorPages = internalIndex.getGeneratorPages(generator.getName());
        }
        if(generatorPages == null) {
            generatorPages = new ArrayList<>();
        }
        generator.startGeneration(generatorPages);
    }

// Utilities
//----------------------------------------------------------------------------------------------------------------------

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
