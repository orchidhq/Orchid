package com.eden.orchid.languages.highlighter.tags

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.languages.highlighter.SyntaxHighlighterModule
import com.eden.orchid.strikt.htmlBodyMatchesString
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.contains

class HighlightTagTest : OrchidIntegrationTest(
    withGenerator<HomepageGenerator>(),
    SyntaxHighlighterModule()
) {

    @Test
    @DisplayName("Test that the `kotlinPlayground` component is set up properly")
    fun test01() {
        resource(
            "homepage.md",
            """
            |---
            |---
            |{% highlight 'java' %}
            |public class MainClass {
            |   public static void main(String... args) {
            |       System.out.println("Running from Kotlin Playground!")
            |   }
            |}
            |{% endhighlight %}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatchesString {
                    it.contains("<a name=\"line-1\"></a>")
                }
            }
    }
}
