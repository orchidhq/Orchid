package com.eden.orchid.pages

import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@DisplayName("Tests that pages are correctly configured with its archetypes")
class PageConfigurationsTest : OrchidIntegrationTest(PagesModule()) {

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
        resource("pages/page-1.md")
        expectThat(execute())
                .pageWasRendered("/page-1/index.html")
                .get { origin }
                .get { layout }
                .isEqualTo("layoutone")
    }

    @Test
    @DisplayName("Test StaticPages archetype is applied")
    fun test02() {
        configObject("pages", """{"staticPages": {"layout": "layoutone"}}""")
        resource("pages/page-1.md")
        expectThat(execute())
                .pageWasRendered("/page-1/index.html")
                .get { origin }
                .get { layout }
                .isEqualTo("layoutone")
    }

    @Test
    @DisplayName("Test PageGroup archetype is applied")
    fun test03() {
        configObject("pages", """{"groupone": {"layout": "layoutone"}}""")
        resource("pages/groupone/page-1.md")
        expectThat(execute())
                .pageWasRendered("/groupone/page-1/index.html")
                .get { origin }
                .get { layout }
                .isEqualTo("layoutone")
    }

    @Test
    @DisplayName("Test StaticPages archetype takes precedence over AllPages")
    fun test04() {
        configObject("allPages", """{"layout": "layoutone"}""")
        configObject("pages", """{"staticPages": {"layout": "layouttwo"}}""")

        resource("pages/page-1.md")
        expectThat(execute())
                .pageWasRendered("/page-1/index.html")
                .get { origin }
                .get { layout }
                .isEqualTo("layouttwo")
    }

    @Test
    @DisplayName("Test PageGroup archetype takes precedence over StaticPages")
    fun test05() {
        configObject("pages", """{"staticPages": {"layout": "layoutone"}}""")
        configObject("pages", """{"groupone": {"layout": "layouttwo"}}""")

        resource("pages/groupone/page-1.md")
        expectThat(execute())
                .pageWasRendered("/groupone/page-1/index.html")
                .get { origin }
                .get { layout }
                .isEqualTo("layouttwo")
    }

    @Test
    @DisplayName("Test PageGroup, StaticPages, and AllPages archetypes are all applied at the same time")
    fun test06() {
        configObject("allPages", """{"layout": "layoutone"}""")
        configObject("pages", """{"staticPages": {"layout": "layouttwo"}}""")
        configObject("pages", """{"groupone": {"layout": "layoutthree"}}""")
        configObject("pages", """{"grouptwo": {"layout": "layoutfour"}}""")

        resource("pages/page-1.md")
        resource("pages/groupone/page-1.md")
        resource("pages/grouptwo/page-1.md")

        val testResults = execute()

        expectThat(testResults)
                .pageWasRendered("/page-1/index.html")
                .get { origin }
                .get { layout }
                .isEqualTo("layouttwo")

        expectThat(testResults)
                .pageWasRendered("/groupone/page-1/index.html")
                .get { origin }
                .get { layout }
                .isEqualTo("layoutthree")

        expectThat(testResults)
                .pageWasRendered("/grouptwo/page-1/index.html")
                .get { origin }
                .get { layout }
                .isEqualTo("layoutfour")
    }


}
