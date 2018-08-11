package com.eden.orchid.wiki

import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.pageWasRendered
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expect

@DisplayName("Tests page-rendering behavior of Wiki generator")
class WikiGeneratorTest : OrchidIntegrationTest(WikiModule()) {

    @Test
    @DisplayName("Files, formatted correctly in the `wiki` directory, get rendered correctly without any configuration.")
    fun test01() {
        resource("wiki/summary.md", "* [Page One](page-one.md)")
        resource("wiki/page-one.md")

        val testResults = execute()
        expect(testResults).pageWasRendered("/wiki/index.html")
        expect(testResults).pageWasRendered("/wiki/page-one/index.html")
    }

    @Test
    @DisplayName("Wiki pages are found from links in the compiled page content, no matter the source language.")
    fun test02() {
        resource("wiki/summary.html", "<ul><li><a href=\"page-one.md\">Page One</a></li></ul>")
        resource("wiki/page-one.md")

        val testResults = execute()
        expect(testResults).pageWasRendered("/wiki/index.html")
        expect(testResults).pageWasRendered("/wiki/page-one/index.html")
    }

    @Test
    @DisplayName("Wiki supports multiple sections. Setting a section as a string value uses all section defaults.")
    fun tet03() {
        enableLogging()
        config("wiki", mapOf(
                "sections" to listOf("section1", "section2")
        ))
        resource("wiki/section1/summary.md", "* [Page One](page-one.md)")
        resource("wiki/section1/page-one.md")
        resource("wiki/section2/summary.md", "* [Page Two](page-two.md)")
        resource("wiki/section2/page-two.md")

        val testResults = execute()
        expect(testResults).pageWasRendered("/wiki/index.html")
        expect(testResults).pageWasRendered("/wiki/section1/index.html")
        expect(testResults).pageWasRendered("/wiki/section1/page-one/index.html")
        expect(testResults).pageWasRendered("/wiki/section2/index.html")
        expect(testResults).pageWasRendered("/wiki/section2/page-two/index.html")
    }

    @Test
    @DisplayName("Wiki supports multiple sections. You can list each section as an Object to customize its options.")
    fun test04() {
        config("wiki", mapOf(
                "sections" to listOf(
                        mapOf("section1" to mapOf<String, Any?>()),
                        mapOf("section2" to mapOf<String, Any?>())
                )
        ))
        resource("wiki/section1/summary.md", "* [Page One](page-one.md)")
        resource("wiki/section1/page-one.md")
        resource("wiki/section2/summary.md", "* [Page Two](page-two.md)")
        resource("wiki/section2/page-two.md")

        val testResults = execute()
        expect(testResults).pageWasRendered("/wiki/index.html")
        expect(testResults).pageWasRendered("/wiki/section1/index.html")
        expect(testResults).pageWasRendered("/wiki/section1/page-one/index.html")
        expect(testResults).pageWasRendered("/wiki/section2/index.html")
        expect(testResults).pageWasRendered("/wiki/section2/page-two/index.html")
    }

    @Test
    @DisplayName("Wiki supports multiple sections. Rather than a list for the sections, you can use a single Object, where each key points to the options for the value, to query easily.")
    fun test05() {
        config("wiki", mapOf(
                "sections" to mapOf(
                        "section1" to mapOf<String, Any?>(),
                        "section2" to mapOf<String, Any?>()
                )
        ))
        resource("wiki/section1/summary.md", "* [Page One](page-one.md)")
        resource("wiki/section1/page-one.md")
        resource("wiki/section2/summary.md", "* [Page Two](page-two.md)")
        resource("wiki/section2/page-two.md")

        val testResults = execute()
        expect(testResults).pageWasRendered("/wiki/index.html")
        expect(testResults).pageWasRendered("/wiki/section1/index.html")
        expect(testResults).pageWasRendered("/wiki/section1/page-one/index.html")
        expect(testResults).pageWasRendered("/wiki/section2/index.html")
        expect(testResults).pageWasRendered("/wiki/section2/page-two/index.html")
    }

}
