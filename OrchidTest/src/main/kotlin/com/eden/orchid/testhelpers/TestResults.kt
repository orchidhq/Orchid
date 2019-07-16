package com.eden.orchid.testhelpers

import com.eden.orchid.api.OrchidContext

class TestResults(
    val testContext: OrchidContext?,
    val renderedPageMap: Map<String, TestRenderer.TestRenderedPage>,
    val isRenderingSuccess: Boolean,
    val thrownException: Throwable?
) {

    override fun toString(): String {
        return "TestResults: " + (if (isRenderingSuccess) "success" else "failure") + " with " + renderedPageMap.size + " pages"
    }

    fun showResults(): String {
        return if (renderedPageMap.isEmpty()) {
            "(empty site)"
        } else {
            renderedPageMap.keys.sorted().joinToString("\n")
        }
    }

    fun printResults(): TestResults {
        println(showResults())
        return this
    }
}
