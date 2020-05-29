package com.eden.orchid.strikt

import com.eden.orchid.utilities.applyIf
import kotlinx.html.DETAILS
import kotlinx.html.HTMLTag
import kotlinx.html.HtmlBlockTag
import kotlinx.html.HtmlTagMarker
import kotlinx.html.TagConsumer
import kotlinx.html.attributesMapOf
import kotlinx.html.visit
import org.jsoup.Jsoup
import org.jsoup.nodes.Comment
import org.jsoup.nodes.Document
import org.jsoup.nodes.Node
import org.jsoup.select.Elements
import org.jsoup.select.NodeFilter
import strikt.api.Assertion

/**
 * Parse an input String to a DOM tree, which can be further evaluated.
 *
 * @see select
 * @see attr
 */
fun Assertion.Builder<String>.asHtml(
    removeComments: Boolean = true
): Assertion.Builder<Document> =
    get("as HTML document") { normalizeDoc(removeComments) }

/**
 * Apply a CSS selector to a document, and evaluate a block of assertions on the selected elements.
 *
 * @see asHtml
 * @see attr
 */
fun Assertion.Builder<Document>.select(
    cssQuery: String,
    selectorAssertion: Assertion.Builder<Elements>.() -> Unit
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
 * Get the value of an attribute on the selected elements, and evaluate a block of assertions on its value.
 *
 * @see asHtml
 * @see select
 */
fun Assertion.Builder<Elements>.attr(
    attrName: String,
    attrAssertion: Assertion.Builder<String?>.() -> Unit
): Assertion.Builder<Elements> =
    assertBlock("attribute '$attrName'") {
        get("with value %s") { this[attrName] }.attrAssertion()
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

open class SUMMARY(initialAttributes: Map<String, String>, override val consumer: TagConsumer<*>) :
    HTMLTag("summary", consumer, initialAttributes, null, false, false), HtmlBlockTag {
}

@HtmlTagMarker
fun DETAILS.summary(classes: String? = null, block: SUMMARY.() -> Unit = {}): Unit =
    SUMMARY(attributesMapOf("class", classes), consumer).visit(block)
