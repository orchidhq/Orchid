package com.eden.orchid.impl.generators

import com.eden.orchid.strikt.asHtml
import com.eden.orchid.strikt.innerHtmlMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.strikt.printResults
import com.eden.orchid.strikt.select
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
        expectThat(execute())
            .and { get { isRenderingSuccess }.isTrue() }
            .and { get { thrownException }.isNull() }
            .pageWasRendered("/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches { +"" }
                    }
            }
    }

    @Test
    @DisplayName("Homepage generator runs fine")
    fun test02() {
        expectThat(execute())
            .and { get { isRenderingSuccess }.isTrue() }
            .and { get { thrownException }.isNull() }
            .pageWasRendered("/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches { +"" }
                    }
            }
    }

    @Test
    @DisplayName("Homepage generator runs fine")
    fun test03() {
        expectThat(execute())
            .printResults()
            .and { get { isRenderingSuccess }.isTrue() }
            .and { get { thrownException }.isNull() }
            .pageWasRendered("/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches { +"" }
                    }
            }
    }
}