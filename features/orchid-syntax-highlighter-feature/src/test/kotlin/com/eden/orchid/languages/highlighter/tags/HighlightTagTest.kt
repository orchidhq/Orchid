package com.eden.orchid.languages.highlighter.tags

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.languages.highlighter.SyntaxHighlighterModule
import com.eden.orchid.strikt.htmlBodyMatchesStringAssertions
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

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
                htmlBodyMatchesStringAssertions {
                    isEqualTo(
                        """
                        |<body>      
                        |  <div class="highlight">
                        |    <pre>
                        |      <span></span>
                        |      <a name="line-1"></a>
                        |      <span class="lineno">1&nbsp;</span>
                        |      <span class="kd">public</span>
                        |      &nbsp;
                        |      <span class="kd">class</span>
                        |      &nbsp;
                        |      <span class="nc">MainClass</span>
                        |      &nbsp;
                        |      <span class="p">{</span>
                        |      <br>
                        |      <a name="line-2"></a>
                        |      <span class="lineno">2&nbsp;</span>
                        |      &nbsp;&nbsp;&nbsp;
                        |      <span class="kd">public</span>
                        |      &nbsp;
                        |      <span class="kd">static</span>
                        |      &nbsp;
                        |      <span class="kt">void</span>
                        |      &nbsp;
                        |      <span class="nf">main</span>
                        |      <span class="p">(</span>
                        |      <span class="n">String</span>
                        |      <span class="p">...</span>
                        |      &nbsp;
                        |      <span class="n">args</span>
                        |      <span class="p">)</span>
                        |      &nbsp;
                        |      <span class="p">{</span>
                        |      <br>
                        |      <a name="line-3"></a>
                        |      <span class="lineno">3&nbsp;</span>
                        |      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        |      <span class="n">System</span>
                        |      <span class="p">.</span>
                        |      <span class="na">out</span>
                        |      <span class="p">.</span>
                        |      <span class="na">println</span>
                        |      <span class="p">(</span>
                        |      <span class="s">"Running&nbsp;from&nbsp;Kotlin&nbsp;Playground!"</span>
                        |      <span class="p">)</span>
                        |      <br>
                        |      <a name="line-4"></a>
                        |      <span class="lineno">4&nbsp;</span>
                        |      &nbsp;&nbsp;&nbsp;
                        |      <span class="p">}</span>
                        |      <br>
                        |      <a name="line-5"></a>
                        |      <span class="lineno">5&nbsp;</span>
                        |      <span class="p">}</span>
                        |      <br>
                        |    </pre>
                        |  </div>
                        |  <br>         
                        |</body>
                        """.trimMargin()
                    )
                }
            }
    }
}



/*



 */
