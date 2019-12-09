package com.eden.orchid.writersblocks.tags

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import com.eden.orchid.writersblocks.WritersBlocksModule
import kotlinx.html.div
import kotlinx.html.h4
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

class AlertTagTest : OrchidIntegrationTest(
    withGenerator<HomepageGenerator>(),
    WritersBlocksModule()
) {

    @Test
    @DisplayName("Test basic Alert tag")
    fun test01() {
        resource(
            "homepage.md",
            """
            |---
            |---
            |{% alert level='warn' headline='A Headline' %}
            |   Alert Content
            |{% endalert %}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    div("alert alert-warn") {
                        attributes["role"] = "alert"
                        h4("alert-heading") {
                            +"A Headline"
                        }
                        +"Alert Content"
                    }
                }
            }
    }
}
