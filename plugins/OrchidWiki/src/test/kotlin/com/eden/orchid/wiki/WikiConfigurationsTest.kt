package com.eden.orchid.wiki

import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@DisplayName("Tests that pages are correctly configured with its archetypes")
class WikiConfigurationsTest : OrchidIntegrationTest(WikiModule()) {

    @BeforeEach
    fun setUp() {
        resource("templates/layouts/layoutone.peb")
        resource("templates/layouts/layouttwo.peb")
        resource("templates/layouts/layoutthree.peb")
        resource("templates/layouts/layoutfour.peb")
    }

    @Test
    @DisplayName("Test AllPages archetype is applied")
    fun test01() {
        configObject("allPages", """{"layout": "layoutone"}""")
        resource("wiki/summary.md", "* [Page One](page-one.md)")
        resource("wiki/page-one.md")
        expectThat(execute())
                .pageWasRendered("/wiki/page-one/index.html")
                .get { origin }
                .get { layout }
                .isEqualTo("layoutone")
    }

    @Test
    @DisplayName("Test WikiPages archetype is applied")
    fun test02() {
        configObject("wiki", """{"wikiPages": {"layout": "layoutone"}}""")
        resource("wiki/summary.md", "* [Page One](page-one.md)")
        resource("wiki/page-one.md")
        expectThat(execute())
                .pageWasRendered("/wiki/page-one/index.html")
                .get { origin }
                .get { layout }
                .isEqualTo("layoutone")
    }

    @Test
    @DisplayName("Test WikiSection archetype is applied")
    fun test03() {
        configObject("wiki", """{"sections": ["section1"]}""")
        configObject("wiki", """{"section1": {"layout": "layoutone"}}""")
        resource("wiki/section1/summary.md", "* [Page One](page-one.md)")
        resource("wiki/section1/page-one.md")
        expectThat(execute())
                .pageWasRendered("/wiki/section1/page-one/index.html")
                .get { origin }
                .get { layout }
                .isEqualTo("layoutone")
    }

    @Test
    @DisplayName("Test WikiPages archetype takes precedence over AllPages")
    fun test04() {
        configObject("allPages", """{"layout": "layoutone"}""")
        configObject("wiki", """{"wikiPages": {"layout": "layouttwo"}}""")

        resource("wiki/summary.md", "* [Page One](page-one.md)")
        resource("wiki/page-one.md")
        expectThat(execute())
                .pageWasRendered("/wiki/page-one/index.html")
                .get { origin }
                .get { layout }
                .isEqualTo("layouttwo")
    }

    @Test
    @DisplayName("Test WikiSection archetype takes precedence over WikiPages")
    fun test05() {
        configObject("allPages", """{"layout": "layoutone"}""")
        configObject("wiki", """{"sections": ["section1"]}""")
        configObject("wiki", """{"section1": {"layout": "layouttwo"}}""")

        resource("wiki/section1/summary.md", "* [Page One](page-one.md)")
        resource("wiki/section1/page-one.md")
        expectThat(execute())
                .pageWasRendered("/wiki/section1/page-one/index.html")
                .get { origin }
                .get { layout }
                .isEqualTo("layouttwo")
    }

    @Test
    @DisplayName("Test WikiSection, WikiPages, and AllPages archetypes are all applied at the same time")
    fun test06() {
        configObject("wiki", """{"sections": ["section1", "section2"]}""")

        configObject("allPages", """{"layout": "layoutone"}""")
        configObject("wiki", """{"wikiPages": {"layout": "layouttwo"}}""")
        configObject("wiki", """{"section1": {"layout": "layoutthree"}}""")
        configObject("wiki", """{"section2": {"layout": "layoutfour"}}""")

        resource("wiki/section1/summary.md", "* [Page One](page-one.md)")
        resource("wiki/section1/page-one.md")
        resource("wiki/section2/summary.md", "* [Page One](page-one.md)")
        resource("wiki/section2/page-one.md")

        val testResults = execute()

        expectThat(testResults)
                .pageWasRendered("/wiki/section1/page-one/index.html")
                .get { origin }
                .get { layout }
                .isEqualTo("layoutthree")

        expectThat(testResults)
                .pageWasRendered("/wiki/section2/page-one/index.html")
                .get { origin }
                .get { layout }
                .isEqualTo("layoutfour")
    }


}
