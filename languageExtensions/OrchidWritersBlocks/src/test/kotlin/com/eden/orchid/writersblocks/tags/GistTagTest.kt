package com.eden.orchid.writersblocks.tags

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import com.eden.orchid.writersblocks.WritersBlocksModule
import kotlinx.html.script
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

class GistTagTest : OrchidIntegrationTest(
    withGenerator<HomepageGenerator>(),
    WritersBlocksModule()
) {

    @Test
    @DisplayName("Test Gist embed")
    fun test01() {
        val gistUser = "cjbrooks12"
        val gistId = "83a11f066388c9fe905ee1bab47ecca8"
        resource(
            "homepage.md",
            """
            |---
            |---
            |{% gist user="$gistUser" id="$gistId" %}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches("body script") {
                    script(src="https://gist.github.com/$gistUser/$gistId.js") {  }
                }
            }
    }
}
