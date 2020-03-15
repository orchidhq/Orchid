package com.eden.orchid.bsdoc

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BSDocThemeTest : OrchidIntegrationTest(
    withGenerator<HomepageGenerator>(),
    BsDocModule()
) {

    @BeforeEach
    internal fun setUp() {
        flag("theme", "BsDoc")
        flag("diagnose", true)
    }

    @Test
    fun test01() {
        resource(
            "homepage.md",
            """
            |Howdy, **yall**
            """.trimMargin()
        )

//        serveOn(8080)

        execute()
    }
}
