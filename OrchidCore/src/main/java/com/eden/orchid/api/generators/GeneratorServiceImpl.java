package com.eden.orchid.api.generators;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.indexing.OrchidIndex;
import com.eden.orchid.api.indexing.OrchidInternalIndex;
import com.eden.orchid.api.options.annotations.BooleanDefault;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.resources.resource.FreeableResource;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

@Singleton
public final class GeneratorServiceImpl implements GeneratorService {

    private final Set<OrchidGenerator> allGenerators;
    private Set<OrchidGenerator> generators;
    private OrchidContext context;

    @Getter
    private BuildMetrics metrics;

    @Option @Getter @Setter
    private String[] enabled;

    @Option @Getter @Setter
    private String[] disabled;

    @Option @Getter @Setter
    private String[] externalIndices;

    @Getter @Setter
    @Option @BooleanDefault(false)
    private boolean parallelIndexing;

    @Getter @Setter
    @Option @BooleanDefault(false)
    private boolean parallelGeneration;

    @Inject
    public GeneratorServiceImpl(Set<OrchidGenerator> generators, BuildMetrics metrics) {
        this.allGenerators = new TreeSet<>(generators);
        this.metrics = metrics;
    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

// Indexing phase
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void startIndexing() {
        generators = this.allGenerators;
        metrics.startIndexing(generators);

        context.clearIndex();

        buildInternalIndex();
        buildExternalIndex();

        context.mergeIndices(context.getInternalIndex(), context.getExternalIndex());

        metrics.stopIndexing();
    }

    private void buildInternalIndex() {
        getFilteredGenerators(parallelIndexing).forEach(this::indexGenerator);
    }

    private void indexGenerator(OrchidGenerator generator) {
        Clog.d("Indexing [{}]", generator.getKey());
        metrics.startIndexingGenerator(generator.getKey());

        JSONElement el = context.query(generator.getKey());
        if (OrchidUtils.elementIsObject(el)) {
            generator.extractOptions(context, (JSONObject) el.getElement());
        }
        else {
            generator.extractOptions(context, new JSONObject());
        }

        List<? extends OrchidPage> generatorPages = generator.startIndexing();

        if (!EdenUtils.isEmpty(generator.getKey()) && generatorPages != null && generatorPages.size() > 0) {
            OrchidInternalIndex index = new OrchidInternalIndex(generator.getKey());
            for (OrchidPage page : generatorPages) {
                page.setGenerator(generator);
                page.setIndexed(true);
                index.addToIndex(generator.getKey() + "/" + page.getReference().getPath(), page);
                freePage(page);
            }
            context.addChildIndex(generator.getKey(), index);

            metrics.stopIndexingGenerator(generator.getKey(), generatorPages.size());
        }
        else {
            metrics.stopIndexingGenerator(generator.getKey(), 0);
        }
    }

    private void buildExternalIndex() {
        if(!EdenUtils.isEmpty(externalIndices)) {
            for (String externalIndex : externalIndices) {
                JSONObject indexJson = this.context.loadAdditionalFile(externalIndex);
                if (indexJson != null) {
                    OrchidIndex index = OrchidIndex.fromJSON(context, indexJson);
                    context.addExternalChildIndex(index);
                }
            }
        }
    }

// Generation Phase
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void startGeneration() {
        metrics.startGeneration();
        getFilteredGenerators(parallelGeneration).forEach(this::useGenerator);
        metrics.stopGeneration();
    }

    private void useGenerator(OrchidGenerator generator) {
        Clog.d("Generating [{}]", generator.getKey());
        metrics.startGeneratingGenerator(generator.getKey());

        List<? extends OrchidPage> generatorPages = null;
        if (!EdenUtils.isEmpty(generator.getKey())) {
            generatorPages = context.getGeneratorPages(generator.getKey());
        }
        if (generatorPages == null) {
            generatorPages = new ArrayList<>();
        }

        Stream<? extends OrchidPage> generatorPagesStream = generator.isParallel() ? generatorPages.parallelStream() : generatorPages.stream();

        Theme generatorTheme = null;
        if (!EdenUtils.isEmpty(generator.getTheme())) {
            generatorTheme = context.findTheme(generator.getTheme());
        }

        if (generatorTheme != null) {
            Clog.d("Applying [{}] theme to [{}] generator", generatorTheme.getClass().getSimpleName(), generator.getKey());
            context.pushTheme(generatorTheme);
            generator.startGeneration(generatorPagesStream);
            generatorTheme.renderAssets();
            context.popTheme();
        }
        else {
            generator.startGeneration(generatorPagesStream);
        }

        metrics.stopGeneratingGenerator(generator.getKey());
    }

    public void onPageGenerated(OrchidPage page, long millis) {
        metrics.onPageGenerated(page, millis);
        freePage(page);
    }

// Utilities
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Stream<OrchidGenerator> getFilteredGenerators(boolean parallel) {
        Stream<OrchidGenerator> generatorStream = (parallel) ? generators.parallelStream() : generators.stream();

        if(!EdenUtils.isEmpty(disabled)) {
            generatorStream = generatorStream
                    .filter(generator -> !OrchidUtils.inArray(generator, disabled, (generator1, s) -> generator1.getKey().equals(s)));
        }

        if(!EdenUtils.isEmpty(enabled)) {
            generatorStream = generatorStream
                    .filter(generator -> OrchidUtils.inArray(generator, enabled, (generator1, s) -> generator1.getKey().equals(s)));
        }

        return generatorStream;
    }

    private void freePage(OrchidPage page) {
        if (page.getResource() instanceof FreeableResource) {
            ((FreeableResource) page.getResource()).free();
        }
    }

}
