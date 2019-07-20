package com.eden.orchid.impl.generators

import com.eden.orchid.strikt.asHtml
import com.eden.orchid.strikt.innerHtmlMatches
import com.eden.orchid.strikt.nothingElseRendered
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.strikt.select
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

class ExternalIndexGeneratorTest : OrchidIntegrationTest(
    withGenerator<HomepageGenerator>(),
    withGenerator<ExternalIndexGenerator>()
) {

    @BeforeEach
    internal fun setUp() {
        enableLogging()
    }

    @Test
    @DisplayName("The ExternalIndexGenerator loads indices from `external.externalIndices`")
    fun test01() {
        configObject("external", """
            {
                "externalIndices": [
                    "https://copper-leaf.github.io/trellis/meta/all.index.json"
                ]
            }
        """.trimIndent())

        resource("homepage.peb", """
            {{ link("Introspection") }}
        """.trimIndent())

        expectThat(execute())
            .pageWasRendered("/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches { +"https://copper-leaf.github.io/trellis/wiki/features/introspection" }
                    }
            }
            .pageWasRendered("/favicon.ico")
            .pageWasRendered("/404.html")
            .nothingElseRendered()
    }

    @Test
    @DisplayName("The ExternalIndexGenerator loads indices from `services.generators.externalIndices` for backward-compatibility")
    fun test02() {
        configObject("services", """
            {
                "generators": {
                    "externalIndices": [
                        "https://copper-leaf.github.io/trellis/meta/all.index.json"
                    ]
                }
            }
        """.trimIndent())

        resource("homepage.peb", """
            {{ link("Introspection") }}
        """.trimIndent())

        expectThat(execute())
            .pageWasRendered("/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches { +"https://copper-leaf.github.io/trellis/wiki/features/introspection" }
                    }
            }
            .pageWasRendered("/favicon.ico")
            .pageWasRendered("/404.html")
            .nothingElseRendered()
    }
}
