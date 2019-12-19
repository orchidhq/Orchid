package com.eden.orchid.writersblocks.tags

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import com.eden.orchid.writersblocks.WritersBlocksModule
import kotlinx.html.br
import kotlinx.html.div
import kotlinx.html.iframe
import kotlinx.html.style
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

class YouTubeTagTest : OrchidIntegrationTest(
    withGenerator<HomepageGenerator>(),
    WritersBlocksModule()
) {

    @Test
    @DisplayName("Test basic YouTube tag as video embed")
    fun test01() {
        resource(
            "homepage.md",
            """
            |---
            |---
            |{% youtube id='IvUU8joBb1Q' %}
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
                        attributes["allow"] = "accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
                        attributes["allowfullscreen"] = ""
                    }
                }
            }
    }

    @Test
    @DisplayName("Test YouTube tag as video embed")
    fun test02() {
        resource(
            "homepage.md",
            """
            |---
            |---
            |{% youtube id='IvUU8joBb1Q' aspectRatio='16:9' allow=['encrypted-media'] %}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    div {
                        style = "position:relative;padding-top:56.25%;"
                        iframe {
                            style = "position:absolute;top:0;left:0;width:100%;height:100%;"
                            src = "https://www.youtube.com/embed/IvUU8joBb1Q"
                            attributes["frameborder"] = "0"
                            attributes["allow"] = "encrypted-media"
                            attributes["allowfullscreen"] = ""
                        }
                    }
                }
            }
    }

    @Test
    @DisplayName("Test YouTube tag as video embed")
    fun test03() {
        resource(
            "homepage.md",
            """
            |---
            |---
            |{% youtube id='IvUU8joBb1Q' aspectRatio='4:3' allow=['encrypted-media', 'gyroscope', 'picture-in-picture'] start='1:06' %}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    div {
                        style = "position:relative;padding-top:75.00%;"
                        iframe {
                            style = "position:absolute;top:0;left:0;width:100%;height:100%;"
                            src = "https://www.youtube.com/embed/IvUU8joBb1Q?start=66"
                            attributes["frameborder"] = "0"
                            attributes["allow"] = "encrypted-media; gyroscope; picture-in-picture"
                            attributes["allowfullscreen"] = ""
                        }
                    }
                }
            }
    }
}
