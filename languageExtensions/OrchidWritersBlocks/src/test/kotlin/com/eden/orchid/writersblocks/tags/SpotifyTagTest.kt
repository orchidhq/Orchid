package com.eden.orchid.writersblocks.tags
  
import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.asHtml
import com.eden.orchid.strikt.innerHtmlMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.strikt.select
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import com.eden.orchid.writersblocks.WritersBlocksModule
import kotlinx.html.div
import kotlinx.html.iframe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

class SpotifyTagTest : OrchidIntegrationTest(withGenerator<HomepageGenerator>(), WritersBlocksModule()) {

//    @Test
    @DisplayName("Test Spotify tag as track embed")
    fun test01() {

        resource(
            "homepage.md",
            """
            |---
            |---
            |{% spotify type='track' id='0Vkk4vLcrUTYODEiuV9ECP' %}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                get { content }
                    .asHtml()
                    .select("div") { // HOW TO select on div with "spotify-embed" class attribute?
                        innerHtmlMatches() {
                            div {
                                // This doesn't compile - how do you specify multiple attributes on a HTML tag?
                                // iframe(src = "https://open.spotify.com/embed/track/0Vkk4vLcrUTYODEiuV9ECP", allow = "encrypted-media") {}
                                iframe {}
                            }
                        }
                    }
            }
    }
}
