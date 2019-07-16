package com.eden.orchid.kss

import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.hasSize

@DisplayName("Tests page-rendering behavior of KSS generator")
class KssGeneratorTest : OrchidIntegrationTest(KssModule()) {

    @Test
    @DisplayName("CSS-like files in assets/css/ are parsed, and pages are generated for each styleguide sectiojn.")
    fun test01() {
        resource(
            "assets/css/test-kss.scss",
            javaClass
                .classLoader
                .getResourceAsStream("test-kss.scss")
                .bufferedReader()
                .readText()
        )

        val testResults = execute()
        expectThat(testResults) {
            get { renderedPageMap }.hasSize(17)

            pageWasRendered("/styleguide/UI/index.html")
            pageWasRendered("/styleguide/1/index.html")
            pageWasRendered("/styleguide/1/1/index.html")
            pageWasRendered("/styleguide/1/1/2/index.html")
        }
    }

}
