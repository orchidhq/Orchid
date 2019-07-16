package com.eden.orchid.strikt

import com.eden.orchid.testhelpers.TestRenderer
import com.eden.orchid.testhelpers.TestResults
import strikt.api.Assertion

fun Assertion.Builder<TestResults>.somethingRendered(): Assertion.Builder<TestResults> =
    assertThat("at least one page was rendered") {
        it.isRenderingSuccess && it.renderedPageMap.isNotEmpty()
    }

fun Assertion.Builder<TestResults>.nothingRendered(): Assertion.Builder<TestResults> =
    assertThat("no pages were rendered") {
        it.isRenderingSuccess && it.renderedPageMap.isEmpty()
    }

fun Assertion.Builder<TestResults>.pagesGenerated(size: Int): Assertion.Builder<TestResults> =
    apply {
        assert("exactly $size pages were rendered") {
            if (!it.isRenderingSuccess)
                fail("rendering was not successful")
            else if (it.renderedPageMap.isEmpty())
                fail("no pages were rendered")
            else if (it.renderedPageMap.size != size)
                fail("only ${it.renderedPageMap.size} pages were rendered")
            else
                pass()
        }
    }

fun Assertion.Builder<TestResults>.pageWasRendered(name: String): Assertion.Builder<TestRenderer.TestRenderedPage> =
    assertThat("page was rendered at $name") {
        it.isRenderingSuccess && it.renderedPageMap[name] != null
    }.get { renderedPageMap[name]!! }

fun Assertion.Builder<TestResults>.pageWasNotRendered(name: String): Assertion.Builder<TestResults> =
    assertThat("page was not rendered at $name") {
        it.isRenderingSuccess && it.renderedPageMap[name] == null
    }

