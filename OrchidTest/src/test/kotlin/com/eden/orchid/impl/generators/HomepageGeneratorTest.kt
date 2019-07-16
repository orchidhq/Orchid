package com.eden.orchid.impl.generators

import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isNull
import strikt.assertions.isTrue

class HomepageGeneratorTest : OrchidIntegrationTest(withGenerator<HomepageGenerator>()) {

    @Test
    @DisplayName("Homepage generator runs fine")
    fun test01() {
        enableLogging()

        val results = execute()

        results.printResults()

        expectThat(results)
            .and { get { isRenderingSuccess }.isTrue() }
            .and { get { thrownException }.isNull() }
            .pageWasRendered("/index.html")
            .get { println("content=$content") }
    }
}