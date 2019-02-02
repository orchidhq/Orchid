package com.eden.orchid.languages.asciidoc

import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.TestGeneratorModule
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
class AsciidocTest : OrchidIntegrationTest(TestGeneratorModule()) {

    @Test
    @DisplayName("Test that Markdown works normally")
    fun test01() {
        resource("homepage.md",
            """
            |**Markdown Page**
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
            .isEqualTo(
                """
                |<p>
                |  <strong>Markdown Page</strong>
                |</p>
                """.trimMargin()
            )
    }

    @Test
    @DisplayName("Test that Asciidoc syntax is not supported when the module is not included. Homepage file will not be found at all.")
    fun test02() {
        resource("homepage.ad",
            """
            |**Unknown Asciidoc Page**
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
    @DisplayName("Test that Asciidoc syntax works when the file ends with .ad when the module is included")
    fun test03() {
        resource("homepage.ad",
            """
            |**Asciidoc Page**
            """.trimMargin()
        )

        val testResults = execute(AsciidocModule())
        expectThat(testResults)
            .pageWasRendered("//index.html")
            .get { content }
            .asHtml(true)
            .select("body")
            .matches()
            .innerHtml()
            .isEqualTo(
                """
                |<div class="paragraph">
                |  <p>
                |    <strong>Asciidoc Page</strong>
                |  </p>
                |</div>
                """.trimMargin()
            )
    }

}