package com.eden.orchid.api.generators;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.indexing.OrchidIndex;
import com.eden.orchid.api.registration.PrioritizedSetFilter;
import com.eden.orchid.api.resources.resource.FreeableResource;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.impl.indexing.OrchidInternalIndex;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Getter @Setter
@Singleton
public final class GeneratorServiceImpl implements GeneratorService {

    private final Set<OrchidGenerator> allGenerators;
    private Set<OrchidGenerator> generators;
    private OrchidContext context;

    private int progress;
    private int maxProgress;
    private int totalPageCount;

    @Inject
    public GeneratorServiceImpl(Set<OrchidGenerator> generators) {
        this.allGenerators = new TreeSet<>(generators);
    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

    @Override
    public void startIndexing() {
        progress = 0;
        totalPageCount = 0;
        generators = new PrioritizedSetFilter<>(context, "generators", this.allGenerators).getFilteredSet();
        maxProgress = this.generators.size();

        context.clearIndex();

        buildInternalIndex();
        buildExternalIndex();

        context.mergeIndices(context.getInternalIndex(), context.getExternalIndex());
        context.broadcast(Orchid.Lifecycle.IndexProgress.fire(this, maxProgress, maxProgress));
    }

    @Override
    public void startGeneration() {
        progress = 0;
        maxProgress = totalPageCount;
        generators.stream()
                  .forEach(this::useGenerator);
        context.broadcast(Orchid.Lifecycle.BuildProgress.fire(this, maxProgress, maxProgress, 0));
    }

// Indexing phase
//----------------------------------------------------------------------------------------------------------------------

    private void buildInternalIndex() {
        generators.stream()
                  .forEach(this::indexGenerator);
    }

    private void indexGenerator(OrchidGenerator generator) {
        Clog.d("Indexing generator: #{$1}:[#{$2 | className}]", generator.getPriority(), generator);

        context.broadcast(Orchid.Lifecycle.IndexProgress.fire(this, progress, maxProgress));

        JSONElement el = context.query(generator.getKey());
        if (OrchidUtils.elementIsObject(el)) {
            generator.extractOptions(context, (JSONObject) el.getElement());
        }
        else {
            generator.extractOptions(context, new JSONObject());
        }

        List<? extends OrchidPage> generatorPages = generator.startIndexing();

        if (!EdenUtils.isEmpty(generator.getKey()) && generatorPages != null && generatorPages.size() > 0) {
            totalPageCount += generatorPages.size();
            OrchidInternalIndex index = new OrchidInternalIndex(generator.getKey());
            for (OrchidPage page : generatorPages) {
                page.setGenerator(generator);
                page.setIndexed(true);
                index.addToIndex(generator.getKey() + "/" + page.getReference().getPath(), page);
                if (page.getResource() instanceof FreeableResource) {
                    ((FreeableResource) page.getResource()).free();
                }
            }
            context.addChildIndex(generator.getKey(), index);
        }

        progress++;
    }

    private void buildExternalIndex() {
        JSONElement externalIndexReferences = context.query("externalIndex");

        if (OrchidUtils.elementIsArray(externalIndexReferences)) {
            JSONArray externalIndex = (JSONArray) externalIndexReferences.getElement();

            for (int i = 0; i < externalIndex.length(); i++) {
                JSONObject indexJson = this.context.loadAdditionalFile(externalIndex.getString(i));
                if (indexJson != null) {
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
        if (!EdenUtils.isEmpty(generator.getKey())) {
            generatorPages = context.getGeneratorPages(generator.getKey());
        }
        if (generatorPages == null) {
            generatorPages = new ArrayList<>();
        }

        Theme generatorTheme = null;
        if (!EdenUtils.isEmpty(generator.getTheme())) {
            generatorTheme = context.findTheme(generator.getTheme());
        }

        if (generatorTheme != null) {
            Clog.d("Applying [{}] theme to [{}] generator", generatorTheme.getClass().getSimpleName(), generator.getKey());
            context.pushTheme(generatorTheme);
            generator.startGeneration(generatorPages);
            generatorTheme.renderAssets();
            context.popTheme();
        }
        else {
            generator.startGeneration(generatorPages);
        }
    }

    public void onPageGenerated(OrchidPage page, long millis) {
        if (page.isIndexed()) {
            progress++;
            context.broadcast(Orchid.Lifecycle.BuildProgress.fire(this, progress, maxProgress, millis));
        }
    }
}
