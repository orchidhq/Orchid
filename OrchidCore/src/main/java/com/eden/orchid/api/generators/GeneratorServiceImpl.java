package com.eden.orchid.api.generators;

import com.caseyjbrooks.clog.Clog;
import com.copperleaf.krow.KrowTable;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.indexing.OrchidIndex;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import kotlin.Lazy;
import kotlin.LazyKt;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @orchidApi services
 * @since v1.0.0
 */
@Singleton
@Description(value = "How content gets created in your build.", name = "Generators")
public final class GeneratorServiceImpl implements GeneratorService {

    private OrchidContext context;
    private BuildMetrics metrics;

    @Option
    @Description("Whitelist the generators in this array, only indexing and generating these generators.")
    private String[] enabled;

    @Option
    @Description("Blacklist the generators in this array, not indexing and generating these generators.")
    private String[] disabled;

    private final Lazy<Set<OrchidGenerator<?>>> allGenerators = LazyKt.lazy(() -> {
        Set<OrchidGenerator<?>> newSet = new TreeSet<>();
        context.resolveSet(OrchidGenerator.class).forEach(newSet::add);
        return newSet;
    });

    @Inject
    public GeneratorServiceImpl() {

    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

    @Override
    public String[] getGeneratorKeys(String[] include, String[] exclude) {
        return getFilteredGenerators(getFilteredGenerators(), include, exclude).map(OrchidGenerator::getKey).toArray(String[]::new);
    }

// Indexing phase
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void startIndexing() {
        try {
            context.popInjector("generating");
        }
        catch (IllegalArgumentException e) {
            // this will throw the first site build. We need to pop the context between builds, but keep it around for
            // the dev server to continue working
        }

        metrics = new BuildMetrics(context);

        metrics.startIndexing(allGenerators.getValue());
        context.clearIndex();
        getFilteredGenerators().forEach(this::indexGenerator);
        metrics.stopIndexing();
    }

    private <T extends OrchidGenerator.Model> void indexGenerator(OrchidGenerator<T> generator) {
        Clog.i("Indexing [{}: {}]", generator.getPriority(), generator.getKey());
        context.broadcast(Orchid.Lifecycle.IndexGeneratorStart.fire(generator));
        metrics.startIndexingGenerator(generator.getKey());
        JSONElement el = context.query(generator.getKey());
        if (EdenUtils.elementIsObject(el)) {
            generator.extractOptions(context, ((JSONObject) el.getElement()).toMap());
        } else {
            generator.extractOptions(context, new HashMap<>());
        }
        // get the pages from a generator
        T generatorModel = generator.startIndexing(context);
        List<? extends OrchidPage> generatorPages = generatorModel.getAllPages();
        Orchid.Lifecycle.IndexGeneratorExtend extend = Orchid.Lifecycle.IndexGeneratorExtend.fire(generator, generatorPages);
        context.broadcast(extend);
        generatorPages = extend.getGeneratorPages();
        OrchidIndex index = new OrchidIndex(null, generator.getKey());
        if (!EdenUtils.isEmpty(generatorPages)) {
            generatorPages.stream().filter(Objects::nonNull).forEach(page -> {
                page.setGenerator(generator);
                page.setIndexed(true);
                index.addToIndex(generator.getKey() + "/" + page.getReference().getPath(), page);
                page.free();
            });
        }
        context.getIndex().addChildIndex(generator.getKey(), index, generatorModel);
        // get the collections from a generator
        List<? extends OrchidCollection> generatorCollections = generator.getCollections(context, generatorModel);
        if (generatorCollections != null && generatorCollections.size() > 0) {
            context.addCollections(generatorCollections);
        }
        // notify the generator is finished indexing
        metrics.stopIndexingGenerator(generator.getKey(),
                (generatorPages != null && generatorPages.size() > 0)
                        ? generatorPages.size()
                        : 0
        );
        context.broadcast(Orchid.Lifecycle.IndexGeneratorFinish.fire(generator));
    }

// Generation Phase
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void startGeneration() {
        List<OrchidGenerator.Model> uniqueModels = context
                .getIndex()
                .getAllIndexedPages()
                .values()
                .stream()
                .map(it -> it.getSecond())
                .filter(it -> !it.getClass().equals(OrchidGenerator.SimpleModel.class))
                .collect(Collectors.toList());

        context.pushInjector("generating", uniqueModels);

        metrics.startGeneration();
        getFilteredGenerators().forEach(this::useGenerator);
        metrics.stopGeneration();
    }

    private <T extends OrchidGenerator.Model> void useGenerator(OrchidGenerator<T> generator) {
        Clog.i("Generating [{}: {}]", generator.getPriority(), generator.getKey());
        metrics.startGeneratingGenerator(generator.getKey());
        OrchidGenerator.Model generatorModel = context.getIndex().getChildIndex(generator.getKey());
        if (generatorModel == null) {
            throw new IllegalStateException(Clog.format("Generator {} did not have a model registered!", generator.getKey()));
        }

        Theme customTheme = context.doWithTheme(generator.getTheme(), () -> generator.startGeneration(context, (T) generatorModel));
        if (customTheme != null) {
            Clog.d("[{}] Generator pages rendered with [{}] Theme.", generator.getKey(), customTheme.getKey());
        }
        metrics.stopGeneratingGenerator(generator.getKey());
    }

    public void onPageGenerated(OrchidPage page, long millis) {
        metrics.onPageGenerated(page, millis);
        page.free();
    }

// Utilities
//----------------------------------------------------------------------------------------------------------------------

    Stream<OrchidGenerator<?>> getFilteredGenerators() {
        return getFilteredGenerators(enabled, disabled);
    }

    Stream<OrchidGenerator<?>> getFilteredGenerators(String[] include, String[] exclude) {
        return getFilteredGenerators(allGenerators.getValue().stream(), include, exclude);
    }

    Stream<OrchidGenerator<?>> getFilteredGenerators(Stream<OrchidGenerator<?>> generators, String[] include, String[] exclude) {
        Stream<OrchidGenerator<?>> generatorStream = generators;
        if (!EdenUtils.isEmpty(exclude)) {
            generatorStream = generatorStream.filter(generator -> !OrchidUtils.inArray(generator, exclude, (generator1, s) -> generator1.getKey().equals(s)));
        }
        if (!EdenUtils.isEmpty(include)) {
            generatorStream = generatorStream.filter(generator -> OrchidUtils.inArray(generator, include, (generator1, s) -> generator1.getKey().equals(s)));
        }
        return generatorStream;
    }

// Build Details
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String getBuildSummary() {
        return metrics.getSummary();
    }

    @Override
    public KrowTable getBuildDetail() {
        return metrics.getDetail();
    }

    public BuildMetrics getMetrics() {
        return this.metrics;
    }

    public String[] getEnabled() {
        return this.enabled;
    }

    public void setEnabled(final String[] enabled) {
        this.enabled = enabled;
    }

    public String[] getDisabled() {
        return this.disabled;
    }

    public void setDisabled(final String[] disabled) {
        this.disabled = disabled;
    }

}
