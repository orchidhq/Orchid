package com.eden.orchid.api.generators;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter @Setter
public class BuildMetrics {

    private final OrchidContext context;

    @Inject
    public BuildMetrics(OrchidContext context) {
        this.context = context;
    }

    private int progress;
    private int maxProgress;
    private int totalPageCount;

    @Getter private Map<String, GeneratorMetrics> generatorMetricsMap;

// Measure Indexing Phase
//----------------------------------------------------------------------------------------------------------------------

    public void startIndexing(Set<OrchidGenerator> generators) {
        generatorMetricsMap = new HashMap<>();

        progress = 0;
        totalPageCount = 0;
        maxProgress = generators.size();
    }

    public void startIndexingGenerator(String generator) {
        ensureMetricsExist(generator);
        generatorMetricsMap.get(generator).startIndexing();
        context.broadcast(Orchid.Lifecycle.IndexProgress.fire(this, progress, maxProgress));
    }

    public void stopIndexingGenerator(String generator, int numberOfPages) {
        ensureMetricsExist(generator);
        generatorMetricsMap.get(generator).stopIndexing();
        progress++;
        totalPageCount += numberOfPages;
    }

    public void stopIndexing() {
        context.broadcast(Orchid.Lifecycle.IndexProgress.fire(this, maxProgress, maxProgress));
    }

// Measure Generation Phase
//----------------------------------------------------------------------------------------------------------------------

    public void startGeneration() {
        progress = 0;
        maxProgress = totalPageCount;
    }

    public void startGeneratingGenerator(String generator) {
        ensureMetricsExist(generator);
    }

    public void stopGeneratingGenerator(String generator) {
        ensureMetricsExist(generator);
    }

    public void onPageGenerated(OrchidPage page, long millis) {
        if (page.isIndexed()) {
            progress++;
            context.broadcast(Orchid.Lifecycle.BuildProgress.fire(this, progress, maxProgress, millis));

            if (page.getGenerator() != null) {
                ensureMetricsExist(page.getGenerator().getKey());
                generatorMetricsMap.get(page.getGenerator().getKey()).addPageGenerationTime(millis);
            }
        }
    }

    public void stopGeneration() {
        printMetrics();
        context.broadcast(Orchid.Lifecycle.BuildProgress.fire(this, maxProgress, maxProgress, 0));
    }

// Print Metrics
//----------------------------------------------------------------------------------------------------------------------

    private void ensureMetricsExist(String generator) {
        if (generator != null && !generatorMetricsMap.containsKey(generator)) {
            generatorMetricsMap.put(generator, new GeneratorMetrics(generator));
        }
    }

    private void printMetrics() {
        GeneratorMetrics compositeMetrics = new GeneratorMetrics("total");
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

        for (GeneratorMetrics metric : generatorMetricsMap.values()) {
            compositeMetrics.setIndexingStartTime(Math.min(compositeMetrics.getIndexingStartTime(), metric.getIndexingStartTime()));
            compositeMetrics.setIndexingEndTime(Math.min(compositeMetrics.getIndexingEndTime(), metric.getIndexingEndTime()));
            compositeMetrics.setGeneratingStartTime(Math.min(compositeMetrics.getGeneratingStartTime(), metric.getGeneratingStartTime()));
            compositeMetrics.setGeneratingEndTime(Math.min(compositeMetrics.getGeneratingEndTime(), metric.getGeneratingEndTime()));
            compositeMetrics.addAllGenerationTimes(metric.getPageGenerationTimes());

            titleColumnWidth          = Math.max(titleColumnWidth,          metric.getKey().length());
            pageCountColumnWidth      = Math.max(pageCountColumnWidth,      Integer.toString(metric.getPageGenerationTimes().size()).length());
            indexingTimeColumnWidth   = Math.max(indexingTimeColumnWidth,   metric.getIndexingTime().length());
            generationTimeColumnWidth = Math.max(generationTimeColumnWidth, metric.getGeneratingTime().length());
            meanPageTimeColumnWidth   = Math.max(meanPageTimeColumnWidth,   metric.getMeanPageTime().length());
            medianPageTimeColumnWidth = Math.max(medianPageTimeColumnWidth, metric.getMedianPageTime().length());
        }

        titleColumnWidth          = Math.max(titleColumnWidth,          compositeMetrics.getKey().length());
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

        for (GeneratorMetrics metric : generatorMetricsMap.values()) {
            table += Clog.format(rowFormat,
                    StringUtils.leftPad(metric.getKey(), titleColumnWidth),
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
                StringUtils.leftPad(compositeMetrics.getKey(), titleColumnWidth),
                StringUtils.leftPad(Integer.toString(compositeMetrics.getPageGenerationTimes().size()), pageCountColumnWidth),
                StringUtils.leftPad(compositeMetrics.getIndexingTime(), indexingTimeColumnWidth),
                StringUtils.leftPad(compositeMetrics.getGeneratingTime(), generationTimeColumnWidth),
                StringUtils.leftPad(compositeMetrics.getMeanPageTime(), meanPageTimeColumnWidth),
                StringUtils.leftPad(compositeMetrics.getMedianPageTime(), medianPageTimeColumnWidth));

        Clog.i("Build Metrics:\n" + table);
    }

}
