package com.eden.orchid.strikt

import com.eden.orchid.testhelpers.OrchidUnitTest
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class StriktHtmlTest : OrchidUnitTest {

    @Test
    fun testCssSelectorText() {
        val html = """
            <div class="c1">
              <div class="c2">
                <p>Paragraph test</p>
              </div>
            </div>
        """.trimIndent()

        expectThat(html)
            .asHtml()
            .select(".c1 .c2 p")
            .matches()
            .text()
            .isEqualTo("Paragraph test")
    }

    @Test
    fun testCssSelectorAttribute() {
        val html = """
            <div class="c1">
              <div class="c2">
                <p data-attr-key="value">Paragraph test</p>
              </div>
            </div>
        """.trimIndent()

        expectThat(html)
            .asHtml()
            .select(".c1 .c2 p")
            .matches()
            .and {
                attr("attr-key").isEqualTo("value")
            }
            .and {
                attr("data-attr-key").isEqualTo("value")
            }
    }

    @Test
    fun testSelectChild() {
        val html = """
            <div class="c1">
              <div class="c2">
                <p data-attr-key="value">Paragraph test</p>
              </div>
            </div>
            <div class="c3">
              <p data-attr-key="other value">Other Paragraph test</p>
            </div>
        """.trimIndent()

        expectThat(html)
            .asHtml()
            .and {
                select(".c1")
                    .matches()
                    .and {
                        select(".c2 p")
                            .and {
                                attr("attr-key").isEqualTo("value")
                            }
                            .and {
                                attr("data-attr-key").isEqualTo("value")
                            }
                    }
                    .and {
                        select(".c3 p").doesNotMatch()
                    }
            }
            .and {
                select(".c3 p").matches()
            }
    }

}