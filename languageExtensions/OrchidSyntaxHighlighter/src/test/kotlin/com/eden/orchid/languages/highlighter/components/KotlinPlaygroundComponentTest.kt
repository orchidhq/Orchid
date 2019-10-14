package com.eden.orchid.languages.highlighter.components

import com.eden.orchid.languages.highlighter.SyntaxHighlighterModule
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

class KotlinPlaygroundComponentTest : OrchidIntegrationTest(TestGeneratorModule(), SyntaxHighlighterModule()) {

    @Test
    @DisplayName("Test that the `kotlinPlayground` component is set up properly")
    fun test01() {
        resource(
            "homepage.md",
            """
            |# Try Kotlin below!
            |
            |```run-kotlin
            |fun main() {
            |   println("Running from Kotlin Playground!")
            |}
            |```
            |{theme='idea' lines='true'}
            """.trimMargin(),
            """
            |{
            |   "components": [
            |       { "type": "pageContent" },
            |       { "type": "kotlinPlayground" }
            |   ]
            |}
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
            .get { replace("\\s+".toRegex(), "") }
            .isEqualTo(
                """
                |<h1 id="try-kotlin-below">Try Kotlin below!</h1>
                |<pre theme="idea" lines="true">
                |  <code class="language-kotlin" theme="idea" lines="true">fun main() {
                |   println("Running from Kotlin Playground!")
                |} </code>
                |</pre>
                |<script data-selector="pre code[class='language-kotlin']" src="https://unpkg.com/kotlin-playground@1"></script>
                """.trimMargin().replace("\\s+".toRegex(), "")
            )
    }
}
