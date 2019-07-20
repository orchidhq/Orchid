package com.eden.orchid.strikt

import com.eden.orchid.testhelpers.OrchidUnitTest
import kotlinx.html.div
import kotlinx.html.p
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.Assertion
import strikt.api.catching
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import strikt.assertions.throws

class StriktHtmlTest : OrchidUnitTest {

    @Test
    @DisplayName("CSS Selectors can match text")
    fun test01() {
        val html = """
            <div class="c1">
              <div class="c2">
                <p>Paragraph test</p>
              </div>
            </div>
        """.trimIndent()

        expectThat(html)
            .asHtml()
            .select(".c1 .c2 p") {
                matches()
                    .get { text() }
                    .isEqualTo("Paragraph test")
            }
    }

    @Test
    @DisplayName("Attributes create compound assertions as a callback assertion on the value")
    fun test02() {
        val html = """
            <div class="c1">
              <div class="c2">
                <p data-attr-key="value">Paragraph test</p>
              </div>
            </div>
        """.trimIndent()

        expectThat(catching {
            expectThat(html)
                .asHtml()
                .select(".c1 .c2 p") {
                    matches()
                        .attr("attr-key") {
                            isEqualTo("value")
                        }
                }
        }).isNull()

        expectThat(catching {
            expectThat(html)
                .asHtml()
                .select(".c1 .c2 p") {
                    matches()
                        .attr("attr-key") {
                            isEqualTo("other value")
                        }
                }
        }).throws<AssertionError>()
            .get { message }
            .isNotNull()
            .checkAndLog(
                """
                |▼ Expect that "<div class="c1">   <div class="c2">     <p data-attr-key="value">Paragraph test</p>   </div> </div>":
                |  ▼ as HTML document:
                |    ✗ select '.c1 .c2 p'
                |      ▼ <p data-attr-key="value">Paragraph test</p>:
                |        ✓ matches at least one node
                |        ✗ attribute 'attr-key'
                |          ▼ with value "value":
                |            ✗ is equal to "other value" : found "value"
                """.trimMargin()
            )
    }

    @Test
    fun test03() {
        val html = """
            <div class="c1">
              <div class="c2">
                <p>Paragraph test</p>
              </div>
            </div>
            """.trimIndent()

        expectThat(html)
            .asHtml()
            .select(".c1 .c2") {
                matches()
                    .innerHtmlMatches {
                        p {
                            +"Paragraph test"
                        }
                    }
            }
    }

    @Test
    fun test04() {
        val html = """
            <div class="c1">
              <div class="c2">
                <p>Paragraph test</p>
              </div>
            </div>
            """.trimIndent()

        expectThat(html)
            .asHtml()
            .select(".c1 .c2") {
                matches()
                    .outerHtmlMatches {
                        div("c2") {
                            p {
                                +"Paragraph test"
                            }
                        }
                    }
            }
    }

    @Test
    fun test05() {
        val html = """
            <html>
            <head>
              <title>A Title</title>
            </head>
            <body>
              <div class="c1">
                <div class="c2">
                  <p>Paragraph test</p>
                </div>
              </div>
            </body>
            </html>
            """.trimIndent()

        expectThat(html)
            .asHtml()
            .select("body") {
                innerHtmlMatches {
                    div("c1") {
                        div("c2") {
                            p {
                                +"Paragraph test"
                            }
                        }
                    }
                }
            }
    }

    fun Assertion.Builder<String>.checkAndLog(expected: String): Assertion.Builder<String> {
        return get { this.replace("\\s".toRegex(), "") }.isEqualTo(expected.replace("\\s".toRegex(), ""))
    }
}
