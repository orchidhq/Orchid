package com.eden.orchid.writersblocks.functions

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import com.eden.orchid.writersblocks.WritersBlocksModule
import kotlinx.html.p
import kotlinx.html.unsafe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

class EncodeSpacesTagTest : OrchidIntegrationTest(
    withGenerator<HomepageGenerator>(),
    WritersBlocksModule()
) {

    @Test
    @DisplayName("Test pluralize tag.")
    fun test01() {
        resource(
            "homepage.md",
            """
            |---
            |---
            |{{ input|encodeSpaces }}
            """.trimMargin(),
            mapOf(
                "input" to "    dog    "
            )
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p {
                        unsafe { +"&nbsp;&nbsp;&nbsp;&nbsp;dog&nbsp;&nbsp;&nbsp;&nbsp;"}
                    }
                }
            }
    }
}
