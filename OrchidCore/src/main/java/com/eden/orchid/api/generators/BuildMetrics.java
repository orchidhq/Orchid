package com.eden.orchid.api.generators;

import com.caseyjbrooks.clog.Clog;
import com.eden.krow.HorizontalAlignment;
import com.eden.krow.KrowTable;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        context.broadcast(Orchid.Lifecycle.ProgressEvent.fire(this, "indexing", progress, maxProgress));
    }

    public void stopIndexingGenerator(String generator, int numberOfPages) {
        ensureMetricsExist(generator);
        generatorMetricsMap.get(generator).stopIndexing();
        progress++;
        totalPageCount += numberOfPages;
    }

    public void stopIndexing() {
        context.broadcast(Orchid.Lifecycle.ProgressEvent.fire(this, "indexing", maxProgress, maxProgress));
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
            context.broadcast(Orchid.Lifecycle.ProgressEvent.fire(this, "building", progress, maxProgress, millis));

            if (page.getGenerator() != null) {
                ensureMetricsExist(page.getGenerator().getKey());
                generatorMetricsMap.get(page.getGenerator().getKey()).addPageGenerationTime(millis);
            }
        }
    }

    public void stopGeneration() {
        printMetrics();
        context.broadcast(Orchid.Lifecycle.ProgressEvent.fire(this, "building", maxProgress, maxProgress, 0));
    }

// Print Metrics
//----------------------------------------------------------------------------------------------------------------------

    private void ensureMetricsExist(String generator) {
        if (generator != null && !generatorMetricsMap.containsKey(generator)) {
            generatorMetricsMap.put(generator, new GeneratorMetrics(generator));
        }
    }

    private void printMetrics() {
        GeneratorMetrics compositeMetrics = new GeneratorMetrics("TOTAL");

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

        KrowTable table = new KrowTable();

        table.columns(
                "Page Count",
                "Indexing Time",
                "Generation Time",
                "Mean Page Generation Time",
                "Median Page Generation Time"
        );

        List<GeneratorMetrics> metricsList = new ArrayList<>(generatorMetricsMap.values());
        metricsList.add(compositeMetrics);

        for (GeneratorMetrics metric : metricsList) {
            if(metric.getPageCount() == 0) continue;

            table.cell("Page Count",                  metric.getKey(), (cell) -> { cell.setContent("" + metric.getPageCount()); return null; });
            table.cell("Indexing Time",               metric.getKey(), (cell) -> { cell.setContent("" + metric.getIndexingTime()); return null; });
            table.cell("Generation Time",             metric.getKey(), (cell) -> { cell.setContent("" + metric.getGeneratingTime()); return null; });
            table.cell("Mean Page Generation Time",   metric.getKey(), (cell) -> { cell.setContent("" + metric.getMeanPageTime()); return null; });
            table.cell("Median Page Generation Time", metric.getKey(), (cell) -> { cell.setContent("" + metric.getMedianPageTime()); return null; });
        }

        table.column("Page Count",                  (cell) -> {cell.setWrapTextAt(pageCountColumnWidth      ); return null; });
        table.column("Indexing Time",               (cell) -> {cell.setWrapTextAt(indexingTimeColumnWidth   ); return null; });
        table.column("Generation Time",             (cell) -> {cell.setWrapTextAt(generationTimeColumnWidth ); return null; });
        table.column("Mean Page Generation Time",   (cell) -> {cell.setWrapTextAt(meanPageTimeColumnWidth   ); return null; });
        table.column("Median Page Generation Time", (cell) -> {cell.setWrapTextAt(medianPageTimeColumnWidth ); return null; });

        table.table((cell) -> {cell.setHorizontalAlignment(HorizontalAlignment.CENTER); return null; });
        table.row("TOTAL", (cell) -> {cell.setHorizontalAlignment(HorizontalAlignment.RIGHT); return null; });

        String tableDisplay = table.print();

        if(compositeMetrics.getPageCount() == 1) {
            tableDisplay += Clog.format("\nGenerated {} page in {}\n\n", compositeMetrics.getPageCount() + "", compositeMetrics.getTotalTime());
        }
        else {
            tableDisplay += Clog.format("\nGenerated {} pages in {}\n\n", compositeMetrics.getPageCount() + "", compositeMetrics.getTotalTime());
        }


        Clog.tag("\nBuild Metrics").log("\n" + tableDisplay);
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
