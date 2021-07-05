package com.eden.orchid.api.generators

import kotlin.math.floor
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds
import kotlin.time.toDuration

@OptIn(ExperimentalTime::class)
class GeneratorMetrics
@JvmOverloads
constructor(
    var key: String?,

    private var indexingStartTime: Long = Long.MAX_VALUE,
    private var generatingStartTime: Long = Long.MAX_VALUE,
    private var indexingEndTime: Long = Long.MIN_VALUE,
    private var generatingEndTime: Long = Long.MIN_VALUE
) {

    private var pageGenerationTimes = mutableListOf<Long>()

// Get formatted values
// ---------------------------------------------------------------------------------------------------------------------

    internal val indexingTime: Duration
        get() = (indexingEndTime - indexingStartTime).toDuration(DurationUnit.MILLISECONDS)

    internal val generatingTime: Duration
        get() = (generatingEndTime - generatingStartTime).toDuration(DurationUnit.MILLISECONDS)

    internal val totalTime: Duration
        get() = (generatingEndTime - indexingStartTime).toDuration(DurationUnit.MILLISECONDS)

    internal val meanPageTime: Duration
        get() = if (pageGenerationTimes.size > 0) {
            pageGenerationTimes
                .average()
                .toDuration(DurationUnit.MILLISECONDS)
        } else {
            0.milliseconds
        }

    internal val medianPageTime: Duration
        get() {
            if (pageGenerationTimes.size > 0) {
                pageGenerationTimes.sortWith(Comparator.naturalOrder())
                val medianIndex = floor((pageGenerationTimes.size / 2).toDouble()).toInt()
                return pageGenerationTimes[medianIndex].toDuration(DurationUnit.MILLISECONDS)
            } else {
                return 0.milliseconds
            }
        }

    internal val pageCount: Int
        get() = getPageGenerationTimes()!!.size

    internal fun startIndexing() {
        indexingStartTime = System.currentTimeMillis()
    }

    internal fun startGenerating() {
        generatingStartTime = System.currentTimeMillis()
    }

    internal fun stopIndexing() {
        indexingEndTime = System.currentTimeMillis()
    }

    internal fun stopGenerating() {
        generatingEndTime = System.currentTimeMillis()
    }

    internal fun addPageGenerationTime(millis: Long) {
        pageGenerationTimes.add(millis)
    }

    internal fun compose(metric: GeneratorMetrics) {
        indexingStartTime = Math.min(indexingStartTime, metric.indexingStartTime)
        indexingEndTime = Math.max(indexingEndTime, metric.indexingEndTime)
        generatingStartTime = Math.min(generatingStartTime, metric.generatingStartTime)
        generatingEndTime = Math.max(generatingEndTime, metric.generatingEndTime)
        pageGenerationTimes.addAll(metric.getPageGenerationTimes()!!)
    }

    fun getPageGenerationTimes(): List<Long>? {
        return this.pageGenerationTimes
    }

    fun setPageGenerationTimes(pageGenerationTimes: MutableList<Long>) {
        this.pageGenerationTimes = pageGenerationTimes
    }
}
