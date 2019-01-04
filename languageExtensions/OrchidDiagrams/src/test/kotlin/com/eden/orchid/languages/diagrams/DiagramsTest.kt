package com.eden.orchid.languages.diagrams

import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.TestHomepageModule
import com.eden.orchid.testhelpers.asHtml
import com.eden.orchid.testhelpers.innerHtml
import com.eden.orchid.testhelpers.matches
import com.eden.orchid.testhelpers.pageWasRendered
import com.eden.orchid.testhelpers.select
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@DisplayName("Tests behavior of using Asciidoc for the homepage")
class DiagramsTest : OrchidIntegrationTest(TestHomepageModule()) {

    @Test
    @DisplayName("Test that Markdown works normally")
    fun test01() {
        resource("homepage.md",
            """
            |Bob->Alice : hello
            """.trimMargin()
        )

        val testResults = execute()
        expectThat(testResults)
            .pageWasRendered("//index.html")
            .get { content }
            .asHtml(true)
            .select("body")
            .matches()
            .innerHtml()
            .isEqualTo("<p>Bob-&gt;Alice : hello</p>")
    }

    @Test
    @DisplayName("Test that PlantUml syntax is not supported when the module is not included. Homepage file will not be found at all.")
    fun test02() {
        resource("homepage.uml",
            """
            |Bob->Alice : hello
            """.trimMargin()
        )

        val testResults = execute()
        expectThat(testResults)
            .pageWasRendered("//index.html")
            .get { content }
            .asHtml(true)
            .select("body")
            .matches()
            .innerHtml()
            .isEqualTo("")
    }

    @Test
    @DisplayName("Test that Asciidoc syntax works when the file ends with .uml when the module is included")
    fun test03() {
        enableLogging()
        resource("homepage.uml",
            """
            |Bob->Alice : hello
            """.trimMargin()
        )

        val testResults = execute(DiagramsModule())
        testResults.printResults()
        expectThat(testResults)
            .pageWasRendered("//index.svg")
            .get { content }
            .asHtml(true)
            .select("body > svg")
            .matches()
    }

}