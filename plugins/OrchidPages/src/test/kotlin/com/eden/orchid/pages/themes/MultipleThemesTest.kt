package com.eden.orchid.pages.themes

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.Theme
import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.pages.PagesModule
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import com.eden.orchid.utilities.addToSet
import kotlinx.html.p
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import javax.inject.Inject

class MultipleThemesTest : OrchidIntegrationTest(withGenerator<HomepageGenerator>(), PagesModule()) {

    class FakeTheme1 @Inject constructor(context: OrchidContext) : Theme(context, "fakeTheme1")
    class FakeTheme2 @Inject constructor(context: OrchidContext) : Theme(context, "fakeTheme2")

    lateinit var testModule: OrchidModule

    @BeforeEach
    fun setUp() {
        resource(
            "homepage.md",
            """
            |---
            |---
            |homepage theme: {{ theme.key }}
            """.trimMargin()
        )

        resource(
            "pages/page-one.md",
            """
            |---
            |---
            |
            |page one theme: {{ theme.key }}
            """.trimMargin()
        )

        testModule = object : OrchidModule() {
            override fun configure() {
                super.configure()
                addToSet<Theme, FakeTheme1>()
                addToSet<Theme, FakeTheme2>()
            }
        }
    }

// Test using base URLs set from CLI flag
//----------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Test switching themes with theme in generator and in page")
    fun test01() {
        configObject(
            "pages",
            """
            |{
            |   "theme": "fakeTheme1"
            |}
            """.trimMargin()
        )
        resource(
            "pages/inner/page/page-two.md",
            """
            |---
            |theme: fakeTheme2
            |---
            |
            |page two theme: {{ theme.key }}
            """.trimMargin()
        )

        expectThat(execute(testModule))
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p { +"homepage theme: Default" }
                }
            }
            .pageWasRendered("/page-one/index.html") {
                htmlBodyMatches {
                    p { +"page one theme: fakeTheme1" }
                }
            }
            .pageWasRendered("/inner/page/page-two/index.html") {
                htmlBodyMatches {
                    p { +"page two theme: fakeTheme2" }
                }
            }
    }

    @Test
    @DisplayName("Test switching themes with theme only in generator")
    fun test02() {
        configObject(
            "pages",
            """
            |{
            |   "theme": "fakeTheme1"
            |}
            """.trimMargin()
        )
        resource(
            "pages/inner/page/page-two.md",
            """
            |---
            |---
            |
            |page two theme: {{ theme.key }}
            """.trimMargin()
        )

        expectThat(execute(testModule))
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p { +"homepage theme: Default" }
                }
            }
            .pageWasRendered("/page-one/index.html") {
                htmlBodyMatches {
                    p { +"page one theme: fakeTheme1" }
                }
            }
            .pageWasRendered("/inner/page/page-two/index.html") {
                htmlBodyMatches {
                    p { +"page two theme: fakeTheme1" }
                }
            }
    }

    @Test
    @DisplayName("Test switching themes with theme only in page")
    fun test03() {

        resource(
            "pages/inner/page/page-two.md",
            """
            |---
            |theme: fakeTheme2
            |---
            |
            |page two theme: {{ theme.key }}
            """.trimMargin()
        )

        expectThat(execute(testModule))
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p { +"homepage theme: Default" }
                }
            }
            .pageWasRendered("/page-one/index.html") {
                htmlBodyMatches {
                    p { +"page one theme: Default" }
                }
            }
            .pageWasRendered("/inner/page/page-two/index.html") {
                htmlBodyMatches {
                    p { +"page two theme: fakeTheme2" }
                }
            }
    }

    @Test
    @DisplayName("Test switching themes with theme only in page")
    fun test04() {
        flag("theme", "fakeTheme2")
        resource(
            "pages/inner/page/page-two.md",
            """
            |---
            |---
            |
            |page two theme: {{ theme.key }}
            """.trimMargin()
        )

        expectThat(execute(testModule))
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p { +"homepage theme: fakeTheme2" }
                }
            }
            .pageWasRendered("/page-one/index.html") {
                htmlBodyMatches {
                    p { +"page one theme: fakeTheme2" }
                }
            }
            .pageWasRendered("/inner/page/page-two/index.html") {
                htmlBodyMatches {
                    p { +"page two theme: fakeTheme2" }
                }
            }
    }

    @Test
    @DisplayName("Test switching themes with theme only in page")
    fun test05() {
        flag("theme", "fakeTheme2")
        configObject(
            "site",
            """
            |{
            |   "theme": "fakeTheme1"
            |}
            """.trimMargin()
        )
        resource(
            "pages/inner/page/page-two.md",
            """
            |---
            |---
            |
            |page two theme: {{ theme.key }}
            """.trimMargin()
        )

        expectThat(execute(testModule))
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p { +"homepage theme: fakeTheme1" }
                }
            }
            .pageWasRendered("/page-one/index.html") {
                htmlBodyMatches {
                    p { +"page one theme: fakeTheme1" }
                }
            }
            .pageWasRendered("/inner/page/page-two/index.html") {
                htmlBodyMatches {
                    p { +"page two theme: fakeTheme1" }
                }
            }
    }
}
