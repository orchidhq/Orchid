package com.eden.orchid.api.generators

import com.caseyjbrooks.clog.Clog
import com.copperleaf.krow.HorizontalAlignment
import com.copperleaf.krow.KrowTable
import com.eden.orchid.Orchid
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.pages.OrchidPage
import java.util.ArrayList
import java.util.HashMap
import javax.inject.Inject

class BuildMetrics
@Inject
constructor(val context: OrchidContext) {

    var progress: Int = 0
    var maxProgress: Int = 0
    var totalPageCount: Int = 0
    private var generatorMetricsMap = mutableMapOf<String, GeneratorMetrics>()
    var compositeMetrics: GeneratorMetrics? = null

    val summary: String
        get() {
            if (compositeMetrics == null) throw IllegalStateException("Cannot get build summary: build not complete")
            return Clog.format(
                "Generated {} {} in {}",
                "" + compositeMetrics!!.pageCount,
                if (compositeMetrics!!.pageCount == 1) "page" else "pages",
                compositeMetrics!!.totalTime
            )
        }

    val detail: KrowTable
        get() {
            if (compositeMetrics == null) throw IllegalStateException("Cannot get build summary: build not complete")
            titleColumnWidth = "Generator".length
            pageCountColumnWidth = "Page Count".length
            indexingTimeColumnWidth = "Indexing Time".length
            generationTimeColumnWidth = "Generation Time".length
            meanPageTimeColumnWidth = "Mean Page Generation Time".length
            medianPageTimeColumnWidth = "Median Page Generation Time".length
            val table = KrowTable()
            table.columns(
                "Page Count",
                "Indexing Time",
                "Generation Time",
                "Mean Page Generation Time",
                "Median Page Generation Time"
            )
            val metricsList = ArrayList(generatorMetricsMap.values)
            metricsList.add(compositeMetrics)
            for (metric in metricsList) {
                if (metric.pageCount == 0) continue
                table.cell("Page Count", metric.key!!) {
                    content = "" + metric.pageCount
                }
                table.cell("Indexing Time", metric.key!!) {
                    content = "" + metric.indexingTime
                }
                table.cell("Generation Time", metric.key!!) {
                    content = "" + metric.generatingTime
                }
                table.cell("Mean Page Generation Time", metric.key!!) {
                    content = "" + metric.meanPageTime
                }
                table.cell("Median Page Generation Time", metric.key!!) {
                    content = "" + metric.medianPageTime
                }
            }
            table.column("Page Count") {
                wrapTextAt = pageCountColumnWidth

            }
            table.column("Indexing Time") {
                wrapTextAt = indexingTimeColumnWidth
            }
            table.column("Generation Time") {
                wrapTextAt = generationTimeColumnWidth
            }
            table.column("Mean Page Generation Time") {
                wrapTextAt = meanPageTimeColumnWidth
            }
            table.column("Median Page Generation Time") {
                wrapTextAt = medianPageTimeColumnWidth
            }
            table.table {
                horizontalAlignment = HorizontalAlignment.CENTER
            }
            table.row("TOTAL") {
                horizontalAlignment = HorizontalAlignment.RIGHT
            }
            return table
        }

    var titleColumnWidth: Int = 0
    var pageCountColumnWidth: Int = 0
    var indexingTimeColumnWidth: Int = 0
    var generationTimeColumnWidth: Int = 0
    var meanPageTimeColumnWidth: Int = 0
    var medianPageTimeColumnWidth: Int = 0

    // Measure Indexing Phase
    //----------------------------------------------------------------------------------------------------------------------
    fun startIndexing(generators: Set<OrchidGenerator<*>>) {
        generatorMetricsMap = HashMap()
        compositeMetrics = null
        progress = 0
        totalPageCount = 0
        maxProgress = generators.size
    }

    fun startIndexingGenerator(generator: String) {
        ensureMetricsExist(generator)
        generatorMetricsMap[generator]!!.startIndexing()

        context.broadcast(Orchid.Lifecycle.ProgressEvent.fire(this, "indexing", progress, maxProgress))
    }

    fun stopIndexingGenerator(generator: String, numberOfPages: Int) {
        ensureMetricsExist(generator)
        generatorMetricsMap[generator]!!.stopIndexing()
        progress++
        totalPageCount += numberOfPages
    }

    fun stopIndexing() {
        context.broadcast(Orchid.Lifecycle.ProgressEvent.fire(this, "indexing", maxProgress, maxProgress))
    }

// Measure Generation Phase
//----------------------------------------------------------------------------------------------------------------------

    fun startGeneration() {
        progress = 0
        maxProgress = totalPageCount
    }

    fun startGeneratingGenerator(generator: String) {
        ensureMetricsExist(generator)
        generatorMetricsMap[generator]!!.startGenerating()
    }

    fun stopGeneratingGenerator(generator: String) {
        ensureMetricsExist(generator)
        generatorMetricsMap[generator]!!.stopGenerating()
    }

    fun onPageGenerated(page: OrchidPage, millis: Long) {
        if (page.isIndexed) {
            progress++
            context.broadcast(Orchid.Lifecycle.ProgressEvent.fire(this, "building", progress, maxProgress, millis))
            if (page.generator != null) {
                ensureMetricsExist(page.generator.key)
                generatorMetricsMap[page.generator.key]!!.addPageGenerationTime(millis)
            }
        }
    }

    fun stopGeneration() {
        compositeMetrics = GeneratorMetrics("TOTAL")
        generatorMetricsMap
            .values
            .stream()
            .peek { compositeMetrics!!.compose(it) }
            .forEach { this.setColumnWidths(it) }
        setColumnWidths(compositeMetrics!!)
        context.broadcast(Orchid.Lifecycle.ProgressEvent.fire(this, "building", maxProgress, maxProgress, 0))
    }

    // Print Metrics
    //----------------------------------------------------------------------------------------------------------------------
    private fun ensureMetricsExist(generator: String?) {
        if (generator != null && !generatorMetricsMap.containsKey(generator)) {
            generatorMetricsMap[generator] = GeneratorMetrics(generator)
        }
    }

    private fun setColumnWidths(metric: GeneratorMetrics) {
        titleColumnWidth = Math.max(titleColumnWidth, metric.key!!.length)
        pageCountColumnWidth = Math.max(pageCountColumnWidth, ("" + metric.pageCount).length)
        indexingTimeColumnWidth = Math.max(indexingTimeColumnWidth, metric.indexingTime.length)
        generationTimeColumnWidth = Math.max(generationTimeColumnWidth, metric.generatingTime.length)
        meanPageTimeColumnWidth = Math.max(meanPageTimeColumnWidth, metric.meanPageTime.length)
        medianPageTimeColumnWidth = Math.max(medianPageTimeColumnWidth, metric.medianPageTime.length)
    }

    fun setGeneratorMetricsMap(generatorMetricsMap: MutableMap<String, GeneratorMetrics>) {
        this.generatorMetricsMap = generatorMetricsMap
    }

    fun getGeneratorMetricsMap(): Map<String, GeneratorMetrics>? {
        return this.generatorMetricsMap
    }
}
