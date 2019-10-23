package com.eden.orchid.writersblocks.tags

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.TestGeneratorModule
import com.eden.orchid.testhelpers.pageWasRendered
import com.eden.orchid.writersblocks.WritersBlocksModule
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

class SpotifyTagTest : OrchidIntegrationTest(
    TestGeneratorModule(HomepageGenerator::class.java),
    WritersBlocksModule()
) {

    @Test
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
            .pageWasRendered("//index.html")
            .get { content }
            .toString()
            .contains("<div class=\"spotify-embed\">")
    }
}
