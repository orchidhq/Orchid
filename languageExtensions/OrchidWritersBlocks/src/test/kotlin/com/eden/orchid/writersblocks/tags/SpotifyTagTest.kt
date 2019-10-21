package com.eden.orchid.writersblocks.tags
  
import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.asHtml
import com.eden.orchid.strikt.innerHtmlMatches
import com.eden.orchid.strikt.matchCountIs
import com.eden.orchid.strikt.matches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.strikt.select
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import com.eden.orchid.writersblocks.WritersBlocksModule
import kotlinx.html.iframe
import kotlinx.html.p
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

class SpotifyTagTest : OrchidIntegrationTest(withGenerator<HomepageGenerator>(), WritersBlocksModule()) {

    @Test
    @DisplayName("Test Spotify tag as track embed")
    fun test01() {
        serveOn(8080)
        
        resource(
            "homepage.md",
            """
            |---
            |test: true
            |---
            |{% spotify 'track' '0Vkk4vLcrUTYODEiuV9ECP' %}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches() {
                            iframe {
                                src = "..."
                            }
                        }
                    }
            }
    }
}
