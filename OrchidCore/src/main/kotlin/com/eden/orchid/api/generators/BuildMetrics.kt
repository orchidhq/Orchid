package com.eden.orchid.api.generators

import clog.Clog
import clog.dsl.format
import com.copperleaf.krow.builder.column
import com.copperleaf.krow.builder.krow
import com.copperleaf.krow.model.HorizontalAlignment
import com.copperleaf.krow.model.Krow
import com.eden.orchid.Orchid
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.pages.OrchidPage
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

    val detail: Krow.Table
        get() {
            if (compositeMetrics == null) throw IllegalStateException("Cannot get build summary: build not complete")

            return krow {
                header {
                    column("Page Count")
                    column("Indexing Time")
                    column("Generation Time")
                    column("Mean Page Generation Time")
                    column("Median Page Generation Time")
                }

                body {
                    val metricsList = (generatorMetricsMap.values + compositeMetrics).filterNotNull()

                    for (metric in metricsList) {
                        if (metric.pageCount == 0) continue
                        val alignment = if (metric.key == "TOTAL") {
                            HorizontalAlignment.CENTER
                        } else {
                            HorizontalAlignment.LEFT
                        }
                        row(metric.key!!) {
                            cell {
                                content = "" + metric.pageCount
                                horizontalAlignment = alignment
                            }
                            cell {
                                content = "" + metric.indexingTime
                                horizontalAlignment = alignment
                            }
                            cell {
                                content = "" + metric.generatingTime
                                horizontalAlignment = alignment
                            }
                            cell {
                                content = "" + metric.meanPageTime
                                horizontalAlignment = alignment
                            }
                            cell {
                                content = "" + metric.medianPageTime
                                horizontalAlignment = alignment
                            }
                        }
                    }
                }
            }
        }

// Measure Indexing Phase
// ---------------------------------------------------------------------------------------------------------------------

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
// ---------------------------------------------------------------------------------------------------------------------

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
            .forEach { compositeMetrics!!.compose(it) }
        context.broadcast(Orchid.Lifecycle.ProgressEvent.fire(this, "building", maxProgress, maxProgress, 0))
    }

// Print Metrics
// ---------------------------------------------------------------------------------------------------------------------

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
