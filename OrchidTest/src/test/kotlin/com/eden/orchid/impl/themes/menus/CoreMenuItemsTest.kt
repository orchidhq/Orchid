package com.eden.orchid.impl.themes.menus

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.pages.PagesModule
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import kotlinx.html.a
import kotlinx.html.li
import kotlinx.html.ul
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

class CoreMenuItemsTest : OrchidIntegrationTest(withGenerator<HomepageGenerator>(), PagesModule()) {

    @BeforeEach
    fun setUp() {
        resource(
            "homepage.md",
            """
            |---
            |---
            |{% include 'themeMenu.peb' %}
            """.trimMargin()
        )
        resource("pages/page-one.peb", "")
        resource("pages/page-two.peb", "")

        resource("pages/one/page-one-one.peb", "")
        resource("pages/one/page-one-two.peb", "")

        resource("pages/two/page-two-one.peb", "")
        resource("pages/two/page-two-two.peb", "")
    }

// Test using base URLs set from CLI flag
//----------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Test `page` menu item")
    fun test01() {
        configObject(
            "theme",
            """
            |{
            |  "menu": [
            |    {
            |      "type": "page",
            |      "itemId": "Page One"
            |    },
            |    {
            |      "type": "page",
            |      "itemId": "Page Two",
            |      "title": "Something else as the title"
            |    }
            |  ]
            |}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    ul {
                        li { a(href="http://orchid.test/page-one") { +"Page One" }}
                        li { a(href="http://orchid.test/page-two") { +"Something else as the title" }}
                    }
                }
            }
    }

    @Test
    @DisplayName("Test `collectionPages` menu item with just collectionType")
    fun test02() {
        configObject(
            "theme",
            """
            |{
            |  "menu": [
            |    {
            |      "type": "collectionPages",
            |      "collectionType": "pages"
            |    }
            |  ]
            |}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    ul {
                        li { a(href="http://orchid.test/one/page-one-one") { +"Page One One" }}
                        li { a(href="http://orchid.test/one/page-one-two") { +"Page One Two" }}
                        li { a(href="http://orchid.test/two/page-two-one") { +"Page Two One" }}
                        li { a(href="http://orchid.test/two/page-two-two") { +"Page Two Two" }}
                        li { a(href="http://orchid.test/page-one") { +"Page One" }}
                        li { a(href="http://orchid.test/page-two") { +"Page Two" }}
                    }
                }
            }
    }

    @Test
    @DisplayName("Test `collectionPages` menu item with collectionType and collectionId")
    fun test03() {
        configObject(
            "theme",
            """
            |{
            |  "menu": [
            |    {
            |      "type": "collectionPages",
            |      "collectionType": "pages",
            |      "collectionId": "one"
            |    }
            |  ]
            |}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    ul {
                        li { a(href="http://orchid.test/one/page-one-one") { +"Page One One" }}
                        li { a(href="http://orchid.test/one/page-one-two") { +"Page One Two" }}
                    }
                }
            }
    }
}
