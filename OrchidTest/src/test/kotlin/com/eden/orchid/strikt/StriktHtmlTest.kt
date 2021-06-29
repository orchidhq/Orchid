package com.eden.orchid.strikt

import com.eden.orchid.testhelpers.OrchidUnitTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.Assertion
import strikt.api.expectCatching
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isFailure
import strikt.assertions.isNotNull
import strikt.assertions.isSuccess

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

        expectCatching {
            expectThat(html)
                .asHtml()
                .select(".c1 .c2 p") {
                    matches()
                        .attr("attr-key") {
                            isEqualTo("value")
                        }
                }
        }.isSuccess()

        expectCatching {
            expectThat(html)
                .asHtml()
                .select(".c1 .c2 p") {
                    matches()
                        .attr("attr-key") {
                            isEqualTo("other value")
                        }
                }
        }.isFailure().isA<AssertionError>()
            .get { message }
            .isNotNull()
            .checkAndLog(
                """
                |▼ Expect that "<div class="c1">
                |               |  <div class="c2">     
                |               |    <p data-attr-key="value">Paragraph test</p>
                |               |  </div> 
                |               |</div>":
                |  ▼ as HTML document:
                |    ✗ select '.c1 .c2 p'
                |      ▼ <p data-attr-key="value">Paragraph test</p>:
                |        ✓ matches at least one node
                |        ✗ attribute 'attr-key'
                |          ▼ with value "value":
                |            ✗ is equal to "other value" 
                |                    found "value"
                """.trimMargin()
            )
    }

    fun Assertion.Builder<String>.checkAndLog(expected: String): Assertion.Builder<String> {
        return get { this.replace("\\s".toRegex(), "") }.isEqualTo(expected.replace("\\s".toRegex(), ""))
    }
}
