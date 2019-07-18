package com.eden.orchid.strikt

import com.eden.orchid.testhelpers.OrchidUnitTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class IsHtmlSimilarTest : OrchidUnitTest {

    @ParameterizedTest
    @MethodSource("params")
    fun testCssSelectorText(doc1: String, doc2: String, expectedResult: Boolean) {
        expectThat(
            doc1.trimMargin().trim()
                .hasHtmlSimilarTo(
                    doc2.trimMargin().trim()
                )
        ).isEqualTo(expectedResult)
    }

    companion object {
        @JvmStatic
        fun params(): Iterator<Arguments> {
            return sequence<Arguments> {
                yield(Arguments.of("<div>Asdf</div>", "<div>Asdf</div>", true))
                yield(Arguments.of("<div>Asdf</div>", "<div>asdf</div>", false))
                yield(Arguments.of("<div>Asdf</div>", "<a>Asdf</a>", false))

                yield(Arguments.of("<div class='one'>Asdf</div>", "<div class='one'>Asdf</div>", true))
                yield(Arguments.of("<div class='one two'>Asdf</div>", "<div class='two one'>Asdf</div>", true))
                yield(Arguments.of("<div class='one'>Asdf</div>", "<div class='one two'>Asdf</div>", false))

                yield(Arguments.of("<div prop1>Asdf</div>", "<div prop1>Asdf</div>", true))
                yield(Arguments.of("<div prop1>Asdf</div>", "<div prop2>Asdf</div>", false))
                yield(Arguments.of("<div prop1='one'>Asdf</div>", "<div prop1='one'>Asdf</div>", true))
                yield(Arguments.of("<div prop1='one'>Asdf</div>", "<div prop1='two'>Asdf</div>", false))
                yield(Arguments.of("<div prop1 prop2>Asdf</div>", "<div prop2 prop1>Asdf</div>", true))
                yield(Arguments.of("<div prop1>Asdf</div>", "<div data-prop1>Asdf</div>", true))
                yield(Arguments.of("<div data-prop1>Asdf</div>", "<div prop1>Asdf</div>", true))
                yield(Arguments.of("<div data-prop1>Asdf</div>", "<div data-prop1>Asdf</div>", true))

                yield(Arguments.of("<div><p>Asdf</p></div>", "<div><p>Asdf</p></div>", true))
                yield(Arguments.of("<div><p>Asdf</p></div>", "<div><a>Asdf</a></div>", false))

                yield(
                    Arguments.of(
                        """
                    |<div>
                    |  <p>Asdf</p>
                    |  other text
                    |</div>
                    """,
                        """
                    |<div>
                    |  <p>Asdf</p>
                    |  other text
                    |</div>
                    """,
                        true
                    )
                )
                yield(
                    Arguments.of(
                        """
                    |<div>
                    |  <p>Asdf</p>
                    |  other text
                    |</div>
                    """,
                        """
                    |<div>
                    |  other text
                    |  <p>Asdf</p>
                    |</div>
                    """,
                        false
                    )
                )
                yield(
                    Arguments.of(
                        """
                    |<div>
                    |  other text
                    |  <p>Asdf</p>
                    |</div>
                    """,
                        """
                    |<div>
                    |  <p>Asdf</p>
                    |  other text
                    |</div>
                    """,
                        false
                    )
                )

            }.iterator()
        }
    }

}