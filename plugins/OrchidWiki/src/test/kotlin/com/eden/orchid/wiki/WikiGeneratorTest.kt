package com.eden.orchid.wiki

import com.eden.orchid.strikt.asHtml
import com.eden.orchid.strikt.innerHtmlMatches
import com.eden.orchid.strikt.nothingRendered
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.strikt.select
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import kotlinx.html.a
import kotlinx.html.li
import kotlinx.html.ul
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

@DisplayName("Tests page-rendering behavior of Wiki generator")
class WikiGeneratorTest : OrchidIntegrationTest(WikiModule()) {

    @Test
    @DisplayName("Files, formatted correctly in the `wiki` directory, get rendered correctly without any configuration.")
    fun test01() {
        resource("wiki/summary.md", "* [Page One](page-one.md)")
        resource("wiki/page-one.md")

        expectThat(execute())
            .pageWasRendered("/wiki/index.html")
            .pageWasRendered("/wiki/page-one/index.html")
    }

    @Test
    @DisplayName("Wiki pages are found from links in the compiled page content, no matter the source language.")
    fun test02() {
        resource("wiki/summary.html", "<ul><li><a href=\"page-one.md\">Page One</a></li></ul>")
        resource("wiki/page-one.md")

        expectThat(execute())
            .pageWasRendered("/wiki/index.html")
            .pageWasRendered("/wiki/page-one/index.html")
    }

    @Test
    @DisplayName("Wiki supports multiple sections. Setting a section as a string value uses all section defaults.")
    fun tet03() {
        configObject("wiki", """{"sections": ["section1", "section2"]}""")
        resource("wiki/section1/summary.md", "* [Page One](page-one.md)")
        resource("wiki/section1/page-one.md")
        resource("wiki/section2/summary.md", "* [Page Two](page-two.md)")
        resource("wiki/section2/page-two.md")

        expectThat(execute())
            .pageWasRendered("/wiki/index.html")
            .pageWasRendered("/wiki/section1/index.html")
            .pageWasRendered("/wiki/section1/page-one/index.html")
            .pageWasRendered("/wiki/section2/index.html")
            .pageWasRendered("/wiki/section2/page-two/index.html")
    }

    @Test
    @DisplayName("Wiki supports multiple sections. You can list each section as an Object to customize its options.")
    fun test04() {
        configObject("wiki", """{"sections": [{"section1": {}}, {"section2": {}}]}""")
        resource("wiki/section1/summary.md", "* [Page One](page-one.md)")
        resource("wiki/section1/page-one.md")
        resource("wiki/section2/summary.md", "* [Page Two](page-two.md)")
        resource("wiki/section2/page-two.md")

        expectThat(execute())
            .pageWasRendered("/wiki/index.html")
            .pageWasRendered("/wiki/section1/index.html")
            .pageWasRendered("/wiki/section1/page-one/index.html")
            .pageWasRendered("/wiki/section2/index.html")
            .pageWasRendered("/wiki/section2/page-two/index.html")
    }

    @Test
    @DisplayName("Wiki supports multiple sections. Rather than a list for the sections, you can use a single Object, where each key points to the options for the value, to query easily.")
    fun test05() {
        configObject("wiki", """{"sections": {"section1": {}, "section2": {}}}""")
        resource("wiki/section1/summary.md", "* [Page One](page-one.md)")
        resource("wiki/section1/page-one.md")
        resource("wiki/section2/summary.md", "* [Page Two](page-two.md)")
        resource("wiki/section2/page-two.md")

        expectThat(execute())
            .pageWasRendered("/wiki/index.html")
            .pageWasRendered("/wiki/section1/index.html")
            .pageWasRendered("/wiki/section1/page-one/index.html")
            .pageWasRendered("/wiki/section2/index.html")
            .pageWasRendered("/wiki/section2/page-two/index.html")
    }

    @Test
    @DisplayName("The Wiki generator finishes successfully when there are no resources for it.")
    fun test06() {
        expectThat(execute())
            .nothingRendered()
    }

    @Test
    @DisplayName("The Wiki generator finishes successfully when there are no resources for it, when using multiple sections.")
    fun test07() {
        configObject("wiki", """{"sections": ["section1", "section2"]}""")

        expectThat(execute())
            .nothingRendered()
    }

    @Test
    @DisplayName("External links are ignored")
    fun test08() {
        resource(
            "wiki/summary.md", """
            * [Page One](page-one.md)
            * [Page Two](https://www.example.com/)
            """.trimIndent()
        )
        resource("wiki/page-one.md")

        expectThat(execute())
            .pageWasRendered("/wiki/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches {
                            ul {
                                li {
                                    a(href = "http://orchid.test/wiki/page-one") { +"Page One" }
                                }
                                li {
                                    a(href = "https://www.example.com/") { +"Page Two" }
                                }
                            }
                        }
                    }
            }
            .pageWasRendered("/wiki/page-one/index.html")
    }

    @Test
    @DisplayName("Files named index are treated as the index of that directory, rather than including 'index' in the path.")
    fun test09() {
        resource(
            "wiki/summary.md", """
            |* [Page One](page-one.md)
            |  * [Page Two](page-two/index.md)
            |  * [Page Three](page-two/page-three.md)
            """.trimMargin()
        )
        resource("wiki/page-one.md")
        resource("wiki/page-two/index.md")
        resource("wiki/page-two/page-three.md")

        expectThat(execute())
            .pageWasRendered("/wiki/index.html")
            .pageWasRendered("/wiki/page-one/index.html")
            .pageWasRendered("/wiki/page-two/index.html")
            .pageWasRendered("/wiki/page-two/page-three/index.html")
    }

}
