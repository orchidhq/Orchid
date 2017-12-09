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
        generatorMetricsMap.get(generator).startGenerating();
    }

    public void stopGeneratingGenerator(String generator) {
        ensureMetricsExist(generator);
        generatorMetricsMap.get(generator).stopGenerating();
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

        rowFormat = "| #{$1} | #{$2} | #{$3} | #{$4} | #{$5} | #{$6} |\n";
        titleColumnWidth          = "Generator".length();
        pageCountColumnWidth      = "Page Count".length();
        indexingTimeColumnWidth   = "Indexing Time".length();
        generationTimeColumnWidth = "Generation Time".length();
        meanPageTimeColumnWidth   = "Mean Page Generation Time".length();
        medianPageTimeColumnWidth = "Median Page Generation Time".length();

        generatorMetricsMap
                .values()
                .stream()
                .peek(compositeMetrics::compose)
                .forEach(this::setColumnWidths);

        setColumnWidths(compositeMetrics);

        String table = "";
        table += row(
                "Generator",
                "Page Count",
                "Indexing Time",
                "Generation Time",
                "Mean Page Generation Time",
                "Median Page Generation Time"
        );
        table += rowBreak();

        for (GeneratorMetrics metric : generatorMetricsMap.values()) {
            table += row(
                    metric.getKey(),
                    metric.getPageCount() + "",
                    metric.getIndexingTime(),
                    metric.getGeneratingTime(),
                    metric.getMeanPageTime(),
                    metric.getMedianPageTime()
            );
        }

        table += rowBreak();
        table += row(
                compositeMetrics.getKey(),
                compositeMetrics.getPageCount() + "",
                compositeMetrics.getIndexingTime(),
                compositeMetrics.getGeneratingTime(),
                compositeMetrics.getMeanPageTime(),
                compositeMetrics.getMedianPageTime()
        );
        table += rowSpanBreak();
        table += rowSpan(Clog.format("Generated {} pages in {}", compositeMetrics.getPageCount() + "", compositeMetrics.getTotalTime()));
        table += rowSpanBreak();

        Clog.i("Build Metrics:\n" + table);
    }

    private String rowBreak() {
        return Clog.format(rowFormat,
                StringUtils.leftPad("", titleColumnWidth, "-"),
                StringUtils.leftPad("", pageCountColumnWidth, "-"),
                StringUtils.leftPad("", indexingTimeColumnWidth, "-"),
                StringUtils.leftPad("", generationTimeColumnWidth, "-"),
                StringUtils.leftPad("", meanPageTimeColumnWidth, "-"),
                StringUtils.leftPad("", medianPageTimeColumnWidth, "-"));
    }

    private String row(String... cols) {
        return Clog.format(rowFormat,
                StringUtils.leftPad(cols[0], titleColumnWidth),
                StringUtils.leftPad(cols[1], pageCountColumnWidth),
                StringUtils.leftPad(cols[2], indexingTimeColumnWidth),
                StringUtils.leftPad(cols[3], generationTimeColumnWidth),
                StringUtils.leftPad(cols[4], meanPageTimeColumnWidth),
                StringUtils.leftPad(cols[5], medianPageTimeColumnWidth));
    }

    private String rowSpan(String text) {
        return Clog.format("| #{$1} |\n",
                StringUtils.rightPad(text,
                        15
                                + titleColumnWidth
                                + pageCountColumnWidth
                                + indexingTimeColumnWidth
                                + generationTimeColumnWidth
                                + meanPageTimeColumnWidth
                                + medianPageTimeColumnWidth
                )
        );
    }

    private String rowSpanBreak() {
        return Clog.format("| #{$1} |\n",
                StringUtils.rightPad("",
                        15
                                + titleColumnWidth
                                + pageCountColumnWidth
                                + indexingTimeColumnWidth
                                + generationTimeColumnWidth
                                + meanPageTimeColumnWidth
                                + medianPageTimeColumnWidth,
                        "-"
                )
        );
    }

    private void setColumnWidths(GeneratorMetrics metric) {
        titleColumnWidth          = Math.max(titleColumnWidth,          metric.getKey().length());
        pageCountColumnWidth      = Math.max(pageCountColumnWidth,      (metric.getPageCount() + "").length());
        indexingTimeColumnWidth   = Math.max(indexingTimeColumnWidth,   metric.getIndexingTime().length());
        generationTimeColumnWidth = Math.max(generationTimeColumnWidth, metric.getGeneratingTime().length());
        meanPageTimeColumnWidth   = Math.max(meanPageTimeColumnWidth,   metric.getMeanPageTime().length());
        medianPageTimeColumnWidth = Math.max(medianPageTimeColumnWidth, metric.getMedianPageTime().length());
    }

    private String rowFormat;
    private int titleColumnWidth;
    private int pageCountColumnWidth;
    private int indexingTimeColumnWidth;
    private int generationTimeColumnWidth;
    private int meanPageTimeColumnWidth;
    private int medianPageTimeColumnWidth;

}
