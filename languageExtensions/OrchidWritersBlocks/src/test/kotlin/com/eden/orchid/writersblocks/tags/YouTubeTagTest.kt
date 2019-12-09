package com.eden.orchid.writersblocks.tags

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import com.eden.orchid.writersblocks.WritersBlocksModule
import kotlinx.html.iframe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

class YouTubeTagTest : OrchidIntegrationTest(
    withGenerator<HomepageGenerator>(),
    WritersBlocksModule()
) {

    @Test
    @DisplayName("Test YouTube tag as video embed")
    fun test01() {
        resource(
            "homepage.md",
            """
            |---
            |---
            |{% youtube id="IvUU8joBb1Q" %}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    iframe {
                        src = "https://www.youtube.com/embed/IvUU8joBb1Q"
                        width = "560"
                        height = "315"
                        attributes["frameborder"] = "0"
                        attributes["allow"] = "autoplay; encrypted-media"
                        attributes["allowfullscreen"] = ""
                    }
                }
            }
    }
}
