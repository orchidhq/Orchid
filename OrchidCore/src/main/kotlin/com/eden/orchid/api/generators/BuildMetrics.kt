package com.eden.orchid.api.generators

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.Orchid
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.pages.OrchidPage
import com.jakewharton.picnic.BorderStyle
import com.jakewharton.picnic.Table
import com.jakewharton.picnic.TextAlignment
import com.jakewharton.picnic.table
import java.util.*
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

    val detail: Table
        get() {
            if (compositeMetrics == null) throw IllegalStateException("Cannot get build summary: build not complete")
            val metricsList = ArrayList(generatorMetricsMap.values)
            val table = table {
                style {
                    borderStyle = BorderStyle.Hidden
                }
                cellStyle {
                    alignment = TextAlignment.MiddleRight
                    paddingLeft = 1
                    paddingRight = 1
                    borderLeft = true
                    borderRight = true
                }
                header {
                    cellStyle {
                        border = true
                        alignment = TextAlignment.BottomLeft
                    }
                    row {
                        cell("#")
                        cell("Page Count")
                        cell("Indexing Time")
                        cell("Generation Time")
                        cell("Mean Page Generation Time")
                        cell("Median Page Generation Time")
                    }
                }
                body {
                    metricsList.forEach {
                        row {
                            cell(it.key)
                            cell(it.pageCount)
                            cell(it.indexingTime)
                            cell(it.generatingTime)
                            cell(it.meanPageTime)
                            cell(it.medianPageTime)
                        }
                    }
                }
                footer {
                    cellStyle {
                        border = true
                    }
                    row {
                        cell(compositeMetrics!!.key)
                        cell(compositeMetrics!!.pageCount)
                        cell(compositeMetrics!!.indexingTime)
                        cell(compositeMetrics!!.generatingTime)
                        cell(compositeMetrics!!.meanPageTime)
                        cell(compositeMetrics!!.medianPageTime)
                    }
                }
            }
            return table
        }

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
            .forEach { compositeMetrics!!.compose(it) }
        context.broadcast(Orchid.Lifecycle.ProgressEvent.fire(this, "building", maxProgress, maxProgress, 0))
    }

    // Print Metrics
    //----------------------------------------------------------------------------------------------------------------------
    private fun ensureMetricsExist(generator: String?) {
        if (generator != null && !generatorMetricsMap.containsKey(generator)) {
            generatorMetricsMap[generator] = GeneratorMetrics(generator)
        }
    }

    fun setGeneratorMetricsMap(generatorMetricsMap: MutableMap<String, GeneratorMetrics>) {
        this.generatorMetricsMap = generatorMetricsMap
    }

    fun getGeneratorMetricsMap(): Map<String, GeneratorMetrics>? {
        return this.generatorMetricsMap
    }
}
