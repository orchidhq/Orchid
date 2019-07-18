package com.eden.orchid.languages.asciidoc

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.asHtml
import com.eden.orchid.strikt.innerHtmlMatches
import com.eden.orchid.strikt.outerHtmlMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.strikt.select
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import kotlinx.html.div
import kotlinx.html.p
import kotlinx.html.strong
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

@DisplayName("Tests behavior of using Asciidoc for the homepage")
class AsciidocTest : OrchidIntegrationTest(withGenerator<HomepageGenerator>()) {

    @Test
    @DisplayName("Test that Markdown works normally")
    fun test01() {
        resource(
            "homepage.md",
            """
            |**Markdown Page**
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        outerHtmlMatches {
                            p {
                                strong {
                                    +"Markdown Page"
                                }
                            }
                        }
                    }
            }
    }

    @Test
    @DisplayName("Test that Asciidoc syntax is not supported when the module is not included. Homepage file will not be found at all.")
    fun test02() {
        resource(
            "homepage.ad",
            """
            |**Unknown Asciidoc Page**
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches { +"" }
                    }
            }
    }

    @Test
    @DisplayName("Test that Asciidoc syntax works when the file ends with .ad when the module is included")
    fun test03() {
        resource(
            "homepage.ad",
            """
            |**Asciidoc Page**
            """.trimMargin()
        )

        expectThat(execute(AsciidocModule()))
            .pageWasRendered("/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches {
                            div("paragraph") {
                                p {
                                    strong {
                                        +"Asciidoc Page"
                                    }
                                }
                            }
                        }
                    }
            }
    }

}