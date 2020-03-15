package com.eden.orchid.bsdoc

import com.eden.orchid.html5up.futureimperfect.FutureImperfectModule
import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FutureImperfectThemeTest : OrchidIntegrationTest(
    withGenerator<HomepageGenerator>(),
    FutureImperfectModule()
) {

    @BeforeEach
    internal fun setUp() {
        flag("theme", "FutureImperfect")
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
