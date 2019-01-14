package com.eden.orchid.api.theme.pages

import com.eden.orchid.api.OrchidContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mockito.mock
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo

class OrchidReferenceTest {

    lateinit var context: OrchidContext

    @BeforeEach
    internal fun setUp() {
        context = mock(OrchidContext::class.java)
    }

    @ParameterizedTest
    @CsvSource(
        "/one/two/three/four/five, 0, one",
        "/one/two/three/four/five, 1, two",
        "/one/two/three/four/five, 2, three",
        "/one/two/three/four/five, 3, four",
        "/one/two/three/four/five, 4, five",

        "/one/two/three/four/five, -5, one",
        "/one/two/three/four/five, -4, two",
        "/one/two/three/four/five, -3, three",
        "/one/two/three/four/five, -2, four",
        "/one/two/three/four/five, -1, five"
    )
    fun testGetPathSegment(path: String, index: Int, segment: String) {
        val ref = OrchidReference(context, path)

        expectThat(ref.getPathSegment(index)).isEqualTo(segment)
    }

    @ParameterizedTest
    @CsvSource(
        "/one/two/three/four/five, 5",
        "/one/two/three/four/five, -6"
    )
    fun testGetPathSegmentThrowing(path: String, index: Int) {
        val ref = OrchidReference(context, path)

        expectThrows<ArrayIndexOutOfBoundsException> { ref.getPathSegment(index) }
    }

}
