package com.eden.orchid.testhelpers

import org.jsoup.Jsoup
import org.jsoup.nodes.Comment
import org.jsoup.nodes.Document
import org.jsoup.nodes.Node
import org.jsoup.select.Elements
import org.jsoup.select.NodeFilter
import strikt.api.Assertion

fun Assertion.Builder<TestResults>.somethingRendered(): Assertion.Builder<TestResults> =
        passesIf("at least one page was rendered") {
            it.renderingSuccess && it.renderedPageMap.isNotEmpty()
        }

fun Assertion.Builder<TestResults>.nothingRendered(): Assertion.Builder<TestResults> =
        passesIf("no pages were rendered") {
            it.renderingSuccess && it.renderedPageMap.isEmpty()
        }

fun Assertion.Builder<TestResults>.pageWasRendered(name: String): Assertion.Builder<TestRenderer.TestRenderedPage> =
        passesIf("page was rendered at $name") {
            it.renderingSuccess && it.renderedPageMap[name] != null
        }.get { it.renderedPageMap[name]!! }

fun Assertion.Builder<TestResults>.pageWasNotRendered(name: String): Assertion.Builder<TestResults> =
        passesIf("page was not rendered at $name") {
            it.renderingSuccess && it.renderedPageMap[name] == null
        }

// Test formatted HTML
//----------------------------------------------------------------------------------------------------------------------

fun Assertion.Builder<String>.asHtml(removeComments: Boolean = false) = get {
    val doc = Jsoup.parse(it)

    if(removeComments) {
        doc.filter(object : NodeFilter {
            override fun tail(node: Node, depth: Int): NodeFilter.FilterResult {
                return if (node is Comment) {
                    NodeFilter.FilterResult.REMOVE
                }
                else NodeFilter.FilterResult.CONTINUE
            }

            override fun head(node: Node, depth: Int): NodeFilter.FilterResult {
                return if (node is Comment) {
                    NodeFilter.FilterResult.REMOVE
                }
                else NodeFilter.FilterResult.CONTINUE
            }
        })
    }

    doc
}

@JvmName("select_document")
fun Assertion.Builder<Document>.select(cssQuery: String) = get { it.select(cssQuery) }

@JvmName("select_elements")
fun Assertion.Builder<Elements>.select(cssQuery: String) = get { it.select(cssQuery) }

fun Assertion.Builder<Elements>.matches() = passesIf("matches at least one node") { it.isNotEmpty() }

fun Assertion.Builder<Elements>.doesNotMatch() = passesIf("matches no nodes") { it.isEmpty() }

fun Assertion.Builder<Elements>.innerHtml() = get { it.html().trimLines() }

fun Assertion.Builder<Elements>.outerHtml() = get { it.outerHtml().trimLines() }

fun Assertion.Builder<Elements>.text() = get { it.text() }

fun Assertion.Builder<Elements>.attr(attrName: String) = get {
    if(it.hasAttr(attrName))
        it.attr(attrName)
    else if(it.hasAttr("data-$attrName"))
        it.attr("data-$attrName")
    else
        null
}

fun Assertion.Builder<Elements>.hasClass(className: String) = passesIf("has class '%s'") { it.hasClass(className) }


private fun String.trimLines() = this
        .lines()
        .map { it.trimEnd() }
        .joinToString("\n")