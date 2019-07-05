package com.eden.orchid.api.generators

import com.eden.orchid.utilities.makeMillisReadable
import java.util.Comparator
import java.util.function.LongConsumer

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
    //----------------------------------------------------------------------------------------------------------------------

    internal val indexingTime: String
        get() = (indexingEndTime - indexingStartTime).makeMillisReadable()

    internal val generatingTime: String
        get() = (generatingEndTime - generatingStartTime).makeMillisReadable()

    internal val totalTime: String
        get() = (generatingEndTime - indexingStartTime).makeMillisReadable()

    internal val meanPageTime: String
        get() = if (pageGenerationTimes.size > 0) {
            pageGenerationTimes
                .stream()
                .collect(
                    { Averager() },
                    { obj, i -> obj.accept(i) },
                    { obj, other -> obj.combine(other) }
                )
                .average()
                .makeMillisReadable()
        } else {
            "N/A"
        }

    internal val medianPageTime: String
        get() {
            if (pageGenerationTimes.size > 0) {
                pageGenerationTimes.sortWith(Comparator.naturalOrder())
                return pageGenerationTimes[Math.floor((pageGenerationTimes.size / 2).toDouble()).toInt()].makeMillisReadable()
            } else {
                return "N/A"
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

    internal class Averager : LongConsumer {
        private var total: Long = 0
        private var count: Long = 0

        fun average(): Double {
            return if (count > 0) total.toDouble() / count else 0.0
        }

        override fun accept(i: Long) {
            total += i
            count++
        }

        fun combine(other: Averager) {
            total += other.total
            count += other.count
        }
    }

    fun getPageGenerationTimes(): List<Long>? {
        return this.pageGenerationTimes
    }

    fun setPageGenerationTimes(pageGenerationTimes: MutableList<Long>) {
        this.pageGenerationTimes = pageGenerationTimes
    }
}
