package com.eden.orchid.strikt

import com.eden.orchid.testhelpers.OrchidUnitTest
import org.jsoup.Jsoup
import org.jsoup.nodes.Comment
import org.jsoup.nodes.Document
import org.jsoup.nodes.Node
import org.jsoup.select.Elements
import org.jsoup.select.NodeFilter
import org.junit.jupiter.api.Test
import strikt.api.Assertion
import strikt.api.expectThat
import strikt.assertions.isEqualTo

fun Assertion.Builder<String>.asHtml(removeComments: Boolean = false) = get {
    val doc = Jsoup.parse(this).apply {
        outputSettings(Document.OutputSettings().apply {
            indentAmount(2)
            prettyPrint(true)
            outline(true)
        })
    }

    if (removeComments) {
        doc.filter(object : NodeFilter {
            override fun tail(node: Node, depth: Int): NodeFilter.FilterResult {
                return if (node is Comment) {
                    NodeFilter.FilterResult.REMOVE
                } else NodeFilter.FilterResult.CONTINUE
            }

            override fun head(node: Node, depth: Int): NodeFilter.FilterResult {
                return if (node is Comment) {
                    NodeFilter.FilterResult.REMOVE
                } else NodeFilter.FilterResult.CONTINUE
            }
        })
    }

    doc
}

@JvmName("select_document")
fun Assertion.Builder<Document>.select(cssQuery: String) = get { select(cssQuery) }

@JvmName("select_elements")
fun Assertion.Builder<Elements>.select(cssQuery: String) = get { select(cssQuery) }

fun Assertion.Builder<Elements>.matches() = assertThat("matches at least one node") { it.isNotEmpty() }

fun Assertion.Builder<Elements>.matchCountIs(count: Int) = apply {
    assert("matches exactly $count nodes", count) {
        if (it.size == count) pass() else fail(it.size)
    }
}

fun Assertion.Builder<Elements>.doesNotMatch() = assertThat("matches no nodes") { it.isEmpty() }

fun Assertion.Builder<Elements>.innerHtml() = get { html().trimLines() }

fun Assertion.Builder<Elements>.outerHtml() = get { outerHtml().trimLines() }

fun Assertion.Builder<Elements>.text() = get { text() }

fun Assertion.Builder<Elements>.attr(attrName: String) = get {
    if (this.hasAttr(attrName))
        this.attr(attrName)
    else if (this.hasAttr("data-$attrName"))
        this.attr("data-$attrName")
    else
        null
}

fun Assertion.Builder<Elements>.hasClass(className: String) = assertThat("has class '%s'") { it.hasClass(className) }

private fun String.trimLines() = this
    .lines()
    .map { it.trimEnd() }
    .joinToString("\n")