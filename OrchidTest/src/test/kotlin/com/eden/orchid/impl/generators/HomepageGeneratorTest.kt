package com.eden.orchid.impl.generators

import com.eden.orchid.strikt.asHtml
import com.eden.orchid.strikt.innerHtmlMatches
import com.eden.orchid.strikt.nothingElseRendered
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.strikt.select
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import kotlinx.html.em
import kotlinx.html.p
import kotlinx.html.strong
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

class HomepageGeneratorTest : OrchidIntegrationTest(withGenerator<HomepageGenerator>()) {

    @Test
    @DisplayName("Homepage generator creates a homepage and 404 pages by default")
    fun test01() {
        expectThat(execute())
            .pageWasRendered("/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches { +"" }
                    }
            }
            .pageWasRendered("/favicon.ico")
            .pageWasRendered("/404.html")
            .nothingElseRendered()
    }

    @Test
    @DisplayName("If a homepage.md file exists, it will be used")
    fun test02() {
        resource("homepage.md", "**bold** _italic_")

        expectThat(execute())
            .pageWasRendered("/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches {
                            p {
                                strong { +"bold" }
                                em { +"italic" }
                            }
                        }
                    }
            }
            .pageWasRendered("/favicon.ico")
            .pageWasRendered("/404.html")
            .nothingElseRendered()
    }

    @Test
    @DisplayName("The homepage file can be any valid file extension")
    fun test03() {
        resource("homepage.peb", "**bold** _italic_")

        expectThat(execute())
            .pageWasRendered("/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches { +"**bold** _italic_" }
                    }
            }
            .pageWasRendered("/favicon.ico")
            .pageWasRendered("/404.html")
            .nothingElseRendered()
    }




    @Test
    @DisplayName("If a 404.md file exists, it will be used")
    fun test04() {
        resource("404.md", "**bold** _italic_")

        expectThat(execute())
            .pageWasRendered("/index.html")
            .pageWasRendered("/favicon.ico")
            .pageWasRendered("/404.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches {
                            p {
                                strong { +"bold" }
                                em { +"italic" }
                            }
                        }
                    }
            }
            .nothingElseRendered()
    }

    @Test
    @DisplayName("The 404 file can be any valid file extension")
    fun test05() {
        resource("404.peb", "**bold** _italic_")

        expectThat(execute())
            .pageWasRendered("/index.html")
            .pageWasRendered("/favicon.ico")
            .pageWasRendered("/404.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches { +"**bold** _italic_" }
                    }
            }
            .nothingElseRendered()
    }
}
