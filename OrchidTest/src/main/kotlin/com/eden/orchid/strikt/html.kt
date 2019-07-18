package com.eden.orchid.strikt

import kotlinx.html.DIV
import kotlinx.html.div
import kotlinx.html.stream.appendHTML
import org.jsoup.Jsoup
import org.jsoup.nodes.Comment
import org.jsoup.nodes.Document
import org.jsoup.nodes.Node
import org.jsoup.select.Elements
import org.jsoup.select.NodeFilter
import strikt.api.Assertion
import java.io.ByteArrayOutputStream
import java.io.PrintStream

/**
 * Parse an input String to a DOM tree, which can be further evaluated.
 *
 * @see select
 * @see innerHtmlMatches
 * @see outerHtmlMatches
 * @see attr
 */
fun Assertion.Builder<String>.asHtml(
    removeComments: Boolean = true
): Assertion.Builder<Document> =
    get("as HTML document") {
        val doc = Jsoup.parse(this).apply {
            outputSettings(Document.OutputSettings().apply {
                indentAmount(2)
                prettyPrint(true)
                outline(true)
            })
        }

        if (removeComments) {
            doc.filter(CommentFilter)
        }
        doc
    }

/**
 * Apply a CSS selector to a document, and evaluate a block of assertions on the selected elements.
 *
 * @see innerHtmlMatches
 * @see outerHtmlMatches
 */
fun Assertion.Builder<Document>.select(
    cssQuery: String, selectorAssertion:
    Assertion.Builder<Elements>.() -> Unit
): Assertion.Builder<Document> =
    assertBlock("select '$cssQuery'") {
        get { select(cssQuery) }.selectorAssertion()
    }

/**
 * Assert that at least one node was matches (such as by a CSS selector).
 *
 * @see select
 */
fun Assertion.Builder<Elements>.matches(): Assertion.Builder<Elements> =
    assertThat("matches at least one node") { it.isNotEmpty() }

/**
 * Assert that at least one node was matches (such as by a CSS selector).
 *
 * @see select
 */
fun Assertion.Builder<Elements>.matchCountIs(count: Int): Assertion.Builder<Elements> =
    assertThat("matches at least one node") { it.size == count }

/**
 * Check is the subject HTML tree is similar to another HTML tree, built with the Kotlin.HTML builder. The inner HTML
 * of the subject tree is compared against the built tree, and a CSS selector is used to select the candidate elemnts
 * from both the subject and built documents.
 *
 * Two HTML trees are considered similar if:
 *
 * - they both contain the same tags in the same order
 * - the text content of all tags are the same
 * - the attributes on each tag are the same, but not necessarily in the same order
 *
 * @see hasHtmlSimilarTo
 */
fun Assertion.Builder<Elements>.innerHtmlMatches(
    thisSelectorToCheck: String = "body",
    otherSelectorToCheck: String = "body > div",
    matcher: DIV.() -> Unit
): Assertion.Builder<Elements> =
    assertThat("inner HTML matches") {
        val doc1 = it.html()
        val doc2 = ByteArrayOutputStream().apply {
            PrintStream(this).appendHTML().div { matcher() }
        }.toString()

        doc1.hasHtmlSimilarTo(
            doc2,
            thisSelectorToCheck = thisSelectorToCheck,
            otherSelectorToCheck = otherSelectorToCheck
        )
    }

/**
 * Check is the subject HTML tree is similar to another HTML tree, built with the Kotlin.HTML builder. The outer HTML
 * of the subject tree is compared against the built tree, and a CSS selector is used to select the candidate elemnts
 * from both the subject and built documents.
 *
 * Two HTML trees are considered similar if:
 *
 * - they both contain the same tags in the same order
 * - the text content of all tags are the same
 * - the attributes on each tag are the same, but not necessarily in the same order
 *
 * @see hasHtmlSimilarTo
 */
fun Assertion.Builder<Elements>.outerHtmlMatches(
    thisSelectorToCheck: String = "body",
    otherSelectorToCheck: String = "body > div",
    matcher: DIV.() -> Unit
): Assertion.Builder<Elements> =
    assertThat("outer HTML matches") {
        val doc1 = it.outerHtml()
        val doc2 = ByteArrayOutputStream().apply {
            PrintStream(this).appendHTML().div { matcher() }
        }.toString()

        doc1.hasHtmlSimilarTo(
            doc2,
            thisSelectorToCheck = thisSelectorToCheck,
            otherSelectorToCheck = otherSelectorToCheck
        )
    }

/**
 * Get the value of an attribute on the selected elements, and evaluate a block of assertions on its value.
 *
 * @see innerHtmlMatches
 * @see outerHtmlMatches
 */
fun Assertion.Builder<Elements>.attr(
    attrName: String,
    attrAssertion: Assertion.Builder<String?>.() -> Unit
): Assertion.Builder<Elements> =
    assertBlock("attribute '$attrName' with value %s") {
        get { this[attrName] }.attrAssertion()
    }


fun String.trimLines() = this
    .lines()
    .map { it.trimEnd() }
    .joinToString("\n")

private operator fun Elements.get(attrName: String): String? {
    return if (hasAttr(attrName))
        attr(attrName)
    else if (hasAttr("data-$attrName"))
        attr("data-$attrName")
    else
        null
}

object CommentFilter : NodeFilter {
    override fun tail(node: Node, depth: Int) =
        if (node is Comment) NodeFilter.FilterResult.REMOVE else NodeFilter.FilterResult.CONTINUE

    override fun head(node: Node, depth: Int) =
        if (node is Comment) NodeFilter.FilterResult.REMOVE else NodeFilter.FilterResult.CONTINUE
}