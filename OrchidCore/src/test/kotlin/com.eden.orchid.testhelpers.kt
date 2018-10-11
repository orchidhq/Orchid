package com.eden.orchid.testhelpers

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import strikt.api.Assertion

fun Assertion.Builder<TestResults>.somethingRendered() =
        assert("some pages were rendered") {
            if(!it.renderingSuccess) fail()
            else if(it.renderedPageMap.isEmpty()) fail()
            else pass()
        }

fun Assertion.Builder<TestResults>.nothingRendered() =
        assert("no pages were rendered") {
            if(!it.renderingSuccess) fail()
            else if(it.renderedPageMap.isEmpty()) pass()
            else fail()
        }

fun Assertion.Builder<TestResults>.pageWasRendered(name: String) =
        assert("page was rendered at $name") {
            if(!it.renderingSuccess) fail()
            else if(it.renderedPageMap[name] != null) pass()
            else fail()
        }

fun Assertion.Builder<TestResults>.pageWasNotRendered(name: String) =
        assert("page was rendered at $name") {
            if(!it.renderingSuccess) fail()
            else if(it.renderedPageMap[name] != null) fail()
            else pass()
        }

// Test formatted HTML
//----------------------------------------------------------------------------------------------------------------------

fun Assertion.Builder<String>.asHtml() = get { Jsoup.parse(it) }

@JvmName("select_document")
fun Assertion.Builder<Document>.select(cssQuery: String) = get { it.select(cssQuery) }

@JvmName("select_elements")
fun Assertion.Builder<Elements>.select(cssQuery: String) = get { it.select(cssQuery) }

fun Assertion.Builder<Elements>.matches() = passesIf("matches at least one node") { it.isNotEmpty() }

fun Assertion.Builder<Elements>.doesNotMatch() = passesIf("matches no nodes") { it.isEmpty() }

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
