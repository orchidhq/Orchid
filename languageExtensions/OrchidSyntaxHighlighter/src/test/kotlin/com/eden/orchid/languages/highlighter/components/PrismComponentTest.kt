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

class PrismComponentTest : OrchidIntegrationTest(
    withGenerator<HomepageGenerator>(),
    SyntaxHighlighterModule()
) {

    @Test
    @DisplayName("Test that the `kotlinPlayground` component is set up properly")
    fun test01() {
        resource(
            "homepage.md",
            """
            |```kotlin
            |fun main() {
            |   println("Running from Kotlin Playground!")
            |}
            |```
            """.trimMargin(),
            """
            |{
            |   "metaComponents": [
            |       { "type": "prism" }
            |   ]
            |}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        get { html().toString().replace("\\s+".toRegex(), "") }
                            .isEqualTo(
                                """
                                |<pre>
                                |  <code class="language-kotlin">fun main() {
                                |   println("Running from Kotlin Playground!")
                                |} </code>
                                |</pre>
                                |<script src="https://cdnjs.cloudflare.com/ajax/libs/prism/1.17.1/prism.min.js"></script>
                                """.trimMargin().replace("\\s+".toRegex(), "")
                            )
                    }
            }
    }
}
