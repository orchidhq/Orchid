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
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private Map<String, GeneratorMetrics> metrics;

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
        metrics = new HashMap<>();

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
        printMetrics();
        context.broadcast(Orchid.Lifecycle.BuildProgress.fire(this, maxProgress, maxProgress, 0));
    }

    private void ensureMetricsExist(OrchidGenerator generator) {
        if (generator != null && !metrics.containsKey(generator.getKey())) {
            metrics.put(generator.getKey(), new GeneratorMetrics(generator.key, generator.getClass().getSimpleName()));
        }
    }

    private void printMetrics() {
        GeneratorMetrics compositeMetrics = new GeneratorMetrics("total", "Totals");
        compositeMetrics.startIndexing();
        compositeMetrics.stopIndexing();
        compositeMetrics.startGenerating();
        compositeMetrics.stopGenerating();

        int titleColumnWidth = 0;
        int pageCountColumnWidth = 0;
        int indexingTimeColumnWidth = 0;
        int generationTimeColumnWidth = 0;
        int meanPageTimeColumnWidth = 0;
        int medianPageTimeColumnWidth = 0;

        for (GeneratorMetrics metric : metrics.values()) {
            compositeMetrics.setIndexingStartTime(Math.min(compositeMetrics.getIndexingStartTime(), metric.getIndexingStartTime()));
            compositeMetrics.setIndexingEndTime(Math.min(compositeMetrics.getIndexingEndTime(), metric.getIndexingEndTime()));
            compositeMetrics.setGeneratingStartTime(Math.min(compositeMetrics.getGeneratingStartTime(), metric.getGeneratingStartTime()));
            compositeMetrics.setGeneratingEndTime(Math.min(compositeMetrics.getGeneratingEndTime(), metric.getGeneratingEndTime()));
            compositeMetrics.addAllGenerationTimes(metric.getPageGenerationTimes());

            titleColumnWidth          = Math.max(titleColumnWidth,          metric.getTitle().length());
            pageCountColumnWidth      = Math.max(pageCountColumnWidth,      Integer.toString(metric.getPageGenerationTimes().size()).length());
            indexingTimeColumnWidth   = Math.max(indexingTimeColumnWidth,   metric.getIndexingTime().length());
            generationTimeColumnWidth = Math.max(generationTimeColumnWidth, metric.getGeneratingTime().length());
            meanPageTimeColumnWidth   = Math.max(meanPageTimeColumnWidth,   metric.getMeanPageTime().length());
            medianPageTimeColumnWidth = Math.max(medianPageTimeColumnWidth, metric.getMedianPageTime().length());
        }

        titleColumnWidth          = Math.max(titleColumnWidth,          compositeMetrics.getTitle().length());
        pageCountColumnWidth      = Math.max(pageCountColumnWidth,      Integer.toString(compositeMetrics.getPageGenerationTimes().size()).length());
        indexingTimeColumnWidth   = Math.max(indexingTimeColumnWidth,   compositeMetrics.getIndexingTime().length());
        generationTimeColumnWidth = Math.max(generationTimeColumnWidth, compositeMetrics.getGeneratingTime().length());
        meanPageTimeColumnWidth   = Math.max(meanPageTimeColumnWidth,   compositeMetrics.getMeanPageTime().length());
        medianPageTimeColumnWidth = Math.max(medianPageTimeColumnWidth, compositeMetrics.getMedianPageTime().length());

        titleColumnWidth          = Math.max(titleColumnWidth,          "Generator".length());
        pageCountColumnWidth      = Math.max(medianPageTimeColumnWidth, "Page Count".length());
        indexingTimeColumnWidth   = Math.max(indexingTimeColumnWidth,   "Indexing Time".length());
        generationTimeColumnWidth = Math.max(generationTimeColumnWidth, "Generation Time".length());
        meanPageTimeColumnWidth   = Math.max(meanPageTimeColumnWidth,   "Mean Page Generation Time".length());
        medianPageTimeColumnWidth = Math.max(medianPageTimeColumnWidth, "Median Page Generation Time".length());

        String table = "";

        String rowFormat = "| #{$1} | #{$2} | #{$3} | #{$4} | #{$5} | #{$6} |\n";

        table += Clog.format(rowFormat,
                StringUtils.leftPad("Generator", titleColumnWidth),
                StringUtils.leftPad("Page Count", pageCountColumnWidth),
                StringUtils.leftPad("Indexing Time", indexingTimeColumnWidth),
                StringUtils.leftPad("Generation Time", generationTimeColumnWidth),
                StringUtils.leftPad("Mean Page Generation Time", meanPageTimeColumnWidth),
                StringUtils.leftPad("Median Page Generation Time", medianPageTimeColumnWidth));

        table += Clog.format(rowFormat,
                StringUtils.leftPad("", titleColumnWidth, "-"),
                StringUtils.leftPad("", pageCountColumnWidth, "-"),
                StringUtils.leftPad("", indexingTimeColumnWidth, "-"),
                StringUtils.leftPad("", generationTimeColumnWidth, "-"),
                StringUtils.leftPad("", meanPageTimeColumnWidth, "-"),
                StringUtils.leftPad("", medianPageTimeColumnWidth, "-"));

        for (GeneratorMetrics metric : metrics.values()) {
            table += Clog.format(rowFormat,
                    StringUtils.leftPad(metric.getTitle(), titleColumnWidth),
                    StringUtils.leftPad(Integer.toString(metric.getPageGenerationTimes().size()), pageCountColumnWidth),
                    StringUtils.leftPad(metric.getIndexingTime(), indexingTimeColumnWidth),
                    StringUtils.leftPad(metric.getGeneratingTime(), generationTimeColumnWidth),
                    StringUtils.leftPad(metric.getMeanPageTime(), meanPageTimeColumnWidth),
                    StringUtils.leftPad(metric.getMedianPageTime(), medianPageTimeColumnWidth));
        }
        table += Clog.format(rowFormat,
                StringUtils.leftPad("", titleColumnWidth, "-"),
                StringUtils.leftPad("", pageCountColumnWidth, "-"),
                StringUtils.leftPad("", indexingTimeColumnWidth, "-"),
                StringUtils.leftPad("", generationTimeColumnWidth, "-"),
                StringUtils.leftPad("", meanPageTimeColumnWidth, "-"),
                StringUtils.leftPad("", medianPageTimeColumnWidth, "-"));

        table += Clog.format(rowFormat,
                StringUtils.leftPad(compositeMetrics.getTitle(), titleColumnWidth),
                StringUtils.leftPad(Integer.toString(compositeMetrics.getPageGenerationTimes().size()), pageCountColumnWidth),
                StringUtils.leftPad(compositeMetrics.getIndexingTime(), indexingTimeColumnWidth),
                StringUtils.leftPad(compositeMetrics.getGeneratingTime(), generationTimeColumnWidth),
                StringUtils.leftPad(compositeMetrics.getMeanPageTime(), meanPageTimeColumnWidth),
                StringUtils.leftPad(compositeMetrics.getMedianPageTime(), medianPageTimeColumnWidth));

        Clog.i("Build Metrics:\n" + table);
    }

// Indexing phase
//----------------------------------------------------------------------------------------------------------------------

    private void buildInternalIndex() {
        generators.stream()
                  .forEach(this::indexGenerator);
    }

    private void indexGenerator(OrchidGenerator generator) {
        Clog.d("Indexing generator: #{$1}:[#{$2 | className}]", generator.getPriority(), generator);

        ensureMetricsExist(generator);
        metrics.get(generator.getKey()).startIndexing();

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

        metrics.get(generator.getKey()).stopIndexing();
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

        ensureMetricsExist(generator);
        metrics.get(generator.getKey()).startGenerating();

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

        ensureMetricsExist(generator);
        metrics.get(generator.getKey()).stopGenerating();
    }

    public void onPageGenerated(OrchidPage page, long millis) {
        if (page.isIndexed()) {
            progress++;
            context.broadcast(Orchid.Lifecycle.BuildProgress.fire(this, progress, maxProgress, millis));

            if (page.getGenerator() != null) {
                ensureMetricsExist(page.getGenerator());
                metrics.get(page.getGenerator().getKey()).addPageGenerationTime(millis);
            }
        }
    }
}
