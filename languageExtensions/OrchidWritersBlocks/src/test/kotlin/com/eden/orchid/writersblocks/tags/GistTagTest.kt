package com.eden.orchid.writersblocks.tags

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.htmlBodyMatchesString
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import com.eden.orchid.writersblocks.WritersBlocksModule
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class GistTagTest : OrchidIntegrationTest(
    withGenerator<HomepageGenerator>(),
    WritersBlocksModule()
) {

    @Test
    @DisplayName("Test Gist embed")
    fun test01() {
        resource(
            "homepage.md",
            """
            |---
            |---
            |{% gist user="cjbrooks12" id="83a11f066388c9fe905ee1bab47ecca8" %}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatchesString {
                    it.isEqualTo("<script src=\"https://gist.github.com/cjbrooks12/83a11f066388c9fe905ee1bab47ecca8.js\"></script>")
                }
            }
    }
}
