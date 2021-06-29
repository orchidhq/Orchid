package com.eden.orchid.strikt

import com.eden.orchid.testhelpers.TestRenderer
import kotlinx.html.BODY
import kotlinx.html.HEAD
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.stream.appendHTML
import strikt.api.Assertion
import java.io.ByteArrayOutputStream
import java.io.PrintStream

// html <head>
// ---------------------------------------------------------------------------------------------------------------------

fun Assertion.Builder<TestRenderer.TestRenderedPage>.htmlHeadMatches(
    selector: String = "head > *",
    domBuilderSelector: String = "head > *",
    ignoreContentWhitespace: Boolean = false,
    matcher: HEAD.() -> Unit
): Assertion.Builder<TestRenderer.TestRenderedPage> =
    and {
        val expected = ByteArrayOutputStream()
            .apply { PrintStream(this).appendHTML().head { matcher() } }
            .toString()
            .normalizeDoc()
            .select(domBuilderSelector)

        assert("HTML head at selector [$selector] matches DOM", expected) {
            val actual = it.content.normalizeDoc().head().select(selector)
            if (actual.hasHtmlSimilarTo(expected, ignoreContentWhitespace))
                pass()
            else
                fail(
                    actual,
                    """
                    |Expected:
                    |${expected.outerHtml().trimLines().prependIndent("    ")}
                    |
                    |Actual:
                    |${actual.outerHtml().trimLines().prependIndent("    ")}
                    """.trimMargin()
                )
        }
    }

fun Assertion.Builder<TestRenderer.TestRenderedPage>.htmlHeadMatchesStringAssertions(
    selector: String = "head",
    matcher: Assertion.Builder<String>.() -> Unit
): Assertion.Builder<TestRenderer.TestRenderedPage> =
    and {
        get { content.normalizeDoc().head().select(selector).outerHtml() }
            .describedAs("HTML head at selector [$selector] as string")
            .matcher()
    }

// html <body>
// ---------------------------------------------------------------------------------------------------------------------

fun Assertion.Builder<TestRenderer.TestRenderedPage>.htmlBodyMatches(
    selector: String = "body > *",
    domBuilderSelector: String = "body > *",
    ignoreContentWhitespace: Boolean = false,
    matcher: BODY.() -> Unit
): Assertion.Builder<TestRenderer.TestRenderedPage> =
    and {
        val expected = ByteArrayOutputStream()
            .apply { PrintStream(this).appendHTML().body { matcher() } }
            .toString()
            .normalizeDoc()
            .select(domBuilderSelector)

        assert("HTML body at selector [$selector] matches DOM", expected) {
            val actual = it.content.normalizeDoc().select(selector)
            if (actual.hasHtmlSimilarTo(expected, ignoreContentWhitespace))
                pass()
            else
                fail(
                    actual,
                    """
                    |Expected:
                    |${expected.outerHtml().trimLines().prependIndent("    ")}
                    |
                    |Actual:
                    |${actual.outerHtml().trimLines().prependIndent("    ")}
                    """.trimMargin()
                )
        }
    }

fun Assertion.Builder<TestRenderer.TestRenderedPage>.htmlBodyMatchesStringAssertions(
    selector: String = "body",
    matcher: Assertion.Builder<String>.() -> Unit
): Assertion.Builder<TestRenderer.TestRenderedPage> =
    and {
        get { content.normalizeDoc().select(selector).outerHtml() }
            .describedAs("HTML body at selector [$selector] as string")
            .matcher()
    }
