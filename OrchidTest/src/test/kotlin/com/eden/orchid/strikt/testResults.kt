package com.eden.orchid.strikt

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.testhelpers.OrchidUnitTest
import com.eden.orchid.testhelpers.TestRenderer
import com.eden.orchid.testhelpers.TestResults
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.opentest4j.AssertionFailedError
import strikt.api.Assertion
import strikt.api.expectCatching
import strikt.api.expectThat
import strikt.assertions.failed
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.succeeded
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class TestResultsAssertionTests : OrchidUnitTest {

    lateinit var context: OrchidContext
    lateinit var originPage: OrchidPage

    @BeforeEach
    fun setUp() {
        context = mock(OrchidContext::class.java)
        originPage = mock(OrchidPage::class.java)
    }

    private fun testRenderedPage(path: String = "", content: String = ""): Pair<String, TestRenderer.TestRenderedPage> {
        return path to TestRenderer.TestRenderedPage(
            path,
            ByteArrayInputStream(content.toByteArray(Charsets.UTF_8)),
            originPage
        )
    }

    @Test
    @DisplayName("something did render, the `somethingRendered` assertion should not throw")
    fun test01() {
        expectCatching {
            expectThat(
                TestResults(
                    context,
                    mapOf(testRenderedPage()),
                    emptyList(),
                    true,
                    null
                )
            ).somethingRendered()
        }.succeeded()

        expectCatching {
            expectThat(
                TestResults(
                    context,
                    emptyMap(),
                    emptyList(),
                    true,
                    null
                )
            ).somethingRendered()
        }.failed().isA<AssertionFailedError>()
            .get { message }
            .isNotNull()
            .checkAndLog(
                """
                |▼ Expect that TestResults: success with 0 pages:
                |  ✗ at least one page was rendered : no pages were rendered
                """.trimMargin()
            )

        expectCatching {
            expectThat(
                TestResults(
                    context,
                    mapOf(testRenderedPage()),
                    emptyList(),
                    false,
                    null
                )
            ).somethingRendered()
        }.failed().isA<AssertionFailedError>()
            .get { message }
            .isNotNull()
            .checkAndLog(
                """
                |▼ Expect that TestResults: failure with 1 pages:
                |  ✗ at least one page was rendered : rendering was not successful
                """.trimMargin()
            )

        expectCatching {
            expectThat(
                TestResults(
                    context,
                    mapOf(testRenderedPage()),
                    emptyList(),
                    true,
                    RuntimeException()
                )
            ).somethingRendered()
        }.failed().isA<AssertionFailedError>()
            .get { message }
            .isNotNull()
            .checkAndLog(
                """
                |▼ Expect that TestResults: failure with 1 pages:
                |  ✗ at least one page was rendered : rendering was not successful
                """.trimMargin()
            )
    }

    @Test
    @DisplayName("something did render, the `nothingRendered` assertion should not throw")
    fun test02() {
        expectCatching {
            expectThat(
                TestResults(
                    context,
                    mapOf(testRenderedPage()),
                    emptyList(),
                    true,
                    null
                )
            ).nothingRendered()
        }.failed().isA<AssertionFailedError>()
            .get { message }
            .isNotNull()
            .checkAndLog(
                """
                |▼ Expect that TestResults: success with 1 pages:
                |  ✗ no pages were rendered : 1 pages were rendered
                """.trimMargin()
            )

        expectCatching {
            expectThat(
                TestResults(
                    context,
                    emptyMap(),
                    emptyList(),
                    true,
                    null
                )
            ).nothingRendered()
        }.succeeded()

        expectCatching {
            expectThat(
                TestResults(
                    context,
                    mapOf(testRenderedPage()),
                    emptyList(),
                    false,
                    null
                )
            ).nothingRendered()
        }.failed().isA<AssertionFailedError>()
            .get { message }
            .isNotNull()
            .checkAndLog(
                """
                |▼ Expect that TestResults: failure with 1 pages:
                |  ✗ no pages were rendered : rendering was not successful
                """.trimMargin()
            )

        expectCatching {
            expectThat(
                TestResults(
                    context,
                    mapOf(testRenderedPage()),
                    emptyList(),
                    true,
                    RuntimeException()
                )
            ).nothingRendered()
        }.failed().isA<AssertionFailedError>()
            .get { message }
            .isNotNull()
            .checkAndLog(
                """
                |▼ Expect that TestResults: failure with 1 pages:
                |  ✗ no pages were rendered : rendering was not successful
                """.trimMargin()
            )
    }

    @Test
    @DisplayName("pagesGenerated tests that a specific number of pages were rendered")
    fun test03() {
        val underTest = TestResults(
            context,
            mapOf(
                testRenderedPage("/index.html"),
                testRenderedPage("/one/index.html"),
                testRenderedPage("/two/index.html")
            ),
            emptyList(),
            true,
            null
        )

        expectCatching { expectThat(underTest).pagesGenerated(3) }.succeeded()

        expectCatching { expectThat(underTest).pagesGenerated(2) }
            .failed().isA<AssertionFailedError>()
            .get { message }
            .isNotNull()
            .checkAndLog(
                """
                |▼ Expect that TestResults: success with 3 pages:
                |  ✗ exactly 2 pages were rendered : 3 pages were rendered
                """.trimMargin()
            )
    }

    @Test
    @DisplayName("nothingElseRendered fails until all pages have been evaluated with `pageWasRendered` assertions")
    fun test04() {
        val underTest = TestResults(
            context,
            mapOf(
                testRenderedPage("/index.html"),
                testRenderedPage("/one/index.html"),
                testRenderedPage("/two/index.html")
            ),
            emptyList(),
            true,
            null
        )

        expectCatching { expectThat(underTest).nothingElseRendered() }
            .failed().isA<AssertionFailedError>()
            .get { message }
            .isNotNull()
            .checkAndLog(
                """
                |▼ Expect that TestResults: success with 3 pages:
                |  ? all pages have been evaluated
                |  ✗ 3 pages were not evaluated
                |    ✗ /index.html
                |    ✗ /one/index.html
                |    ✗ /two/index.html
                """.trimMargin()
            )

        expectCatching { expectThat(underTest).pageWasRendered("/index.html") }.succeeded()
        expectCatching { expectThat(underTest).nothingElseRendered() }
            .failed().isA<AssertionFailedError>()
            .get { message }
            .isNotNull()
            .checkAndLog(
                """
                |▼ Expect that TestResults: success with 3 pages:
                |  ? all pages have been evaluated
                |  ✗ 2 pages were not evaluated
                |    ✗ /one/index.html
                |    ✗ /two/index.html
                """.trimMargin()
            )

        expectCatching { expectThat(underTest).pageWasRendered("/one/index.html") }.succeeded()
        expectCatching { expectThat(underTest).nothingElseRendered() }
            .failed().isA<AssertionFailedError>()
            .get { message }
            .isNotNull()
            .checkAndLog(
                """
                |▼ Expect that TestResults: success with 3 pages:
                |  ? all pages have been evaluated
                |  ✗ 1 pages were not evaluated
                |    ✗ /two/index.html
                """.trimMargin()
            )

        expectCatching { expectThat(underTest).pageWasRendered("/two/index.html") }.succeeded()
        expectCatching { expectThat(underTest).nothingElseRendered() }.succeeded()
    }

    @Test
    @DisplayName("pageWasRendered tests that a page at a given URL was rendered")
    fun test05() {
        val underTest = TestResults(
            context,
            mapOf(
                testRenderedPage("/index.html")
            ),
            emptyList(),
            true,
            null
        )

        expectCatching { expectThat(underTest).pageWasRendered("/index.html") }.succeeded()
        expectCatching { expectThat(underTest).pageWasRendered("/one/index.html") }
            .failed().isA<AssertionFailedError>()
            .get { message }
            .isNotNull()
            .checkAndLog(
                """
                |▼ Expect that TestResults: success with 1 pages:
                |  ✗ page was rendered at /one/index.html : page was not rendered
                """.trimMargin()
            )
    }

    @Test
    @DisplayName(
        "pageWasRendered tests that a page at a given URL was not rendered. For example, expecting that a " +
                "page was a draft and never actually rendered."
    )
    fun test06() {
        val underTest = TestResults(
            context,
            mapOf(
                testRenderedPage("/index.html")
            ),
            emptyList(),
            true,
            null
        )

        expectCatching { expectThat(underTest).pageWasNotRendered("/index.html") }
            .failed().isA<AssertionFailedError>()
            .get { message }
            .isNotNull()
            .checkAndLog(
                """
                |▼ Expect that TestResults: success with 1 pages:
                |  ✗ page was not rendered at /index.html : page was rendered
                """.trimMargin()
            )
        expectCatching { expectThat(underTest).pageWasNotRendered("/one/index.html") }.succeeded()
    }

    @Test
    @DisplayName("printResults logs the rendered pages for debugging")
    fun test07() {
        val outContent = ByteArrayOutputStream()
        val originalOut = System.out

        System.setOut(PrintStream(outContent))

        // call printResults method with a successful render
        expectThat(
            TestResults(
                context,
                mapOf(
                    testRenderedPage("/index.html"),
                    testRenderedPage("/one/index.html"),
                    testRenderedPage("/two/index.html")
                ),
                emptyList(),
                true,
                null
            )
        ).printResults()

        // assert on what was written to println()
        expectThat(outContent.toString().trim())
            .checkAndLog(
                """
                |/index.html
                |/one/index.html
                |/two/index.html
                """.trimMargin()
            )

        System.setOut(originalOut)
    }

    @Test
    @DisplayName("printResults logs the rendered pages for debugging")
    fun test08() {
        val outContent = ByteArrayOutputStream()
        val originalOut = System.out

        System.setOut(PrintStream(outContent))

        // call printResults method with a successful render
        expectThat(
            TestResults(
                null,
                emptyMap(),
                emptyList(),
                false,
                RuntimeException()
            )
        ).printResults()

        // assert on what was written to println()
        expectThat(outContent.toString().trim())
            .checkAndLog(
                """
                |(empty site)
                """.trimMargin()
            )

        System.setOut(originalOut)
    }

    fun Assertion.Builder<String>.checkAndLog(expected: String): Assertion.Builder<String> {
        return get { this.replace("\\s".toRegex(), "") }.isEqualTo(expected.replace("\\s".toRegex(), ""))
    }
}
