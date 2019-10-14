package com.eden.orchid.languages.highlighter.components

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.languages.highlighter.SyntaxHighlighterModule
import com.eden.orchid.strikt.asHtml
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.strikt.select
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class KotlinPlaygroundComponentTest : OrchidIntegrationTest(withGenerator<HomepageGenerator>(), SyntaxHighlighterModule()) {

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

        expectThat(execute())
            .pageWasRendered("/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        get { html().toString() }
                            .get { replace("\\s+".toRegex(), "") }
                            .isEqualTo(
                                """
                                |<h1 id="try-kotlin-below"><ahref="#try-kotlin-below"id="try-kotlin-below"class="anchor"></a>Try Kotlin below!</h1>
                                |<pre theme="idea" lines="true">
                                |  <code class="language-run-kotlin">fun main() {
                                |   println("Running from Kotlin Playground!")
                                |} </code>
                                |</pre>
                                |<script data-selector="pre code[class='language-run-kotlin']" src="https://unpkg.com/kotlin-playground@1"></script>
                                """.trimMargin().replace("\\s+".toRegex(), "")
                            )
                    }
            }
    }
}
