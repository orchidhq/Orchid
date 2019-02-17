package com.eden.orchid.api.theme.pages

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.testhelpers.BaseOrchidTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mockito.mock
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo

class OrchidReferenceTest : BaseOrchidTest() {

    lateinit var context: OrchidContext

    @BeforeEach
    override fun setUp() {
        super.setUp()
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

    @ParameterizedTest
    @CsvSource(
        // basic examples
        "http://www.example.com/example                    ,      ,                   ",
        "https://www.example.com/example                   ,      ,                   ",
        "https://www.example.com/example#one               , one,                     ",
        "https://www.example.com/example?one=two           ,      , one=two           ",
        "https://www.example.com/example?one=two&three=four,      , one=two&three=four",

        // more complex examples
        "http://www.example.com/example.js                 ,      ,                   ",
        "http://www.example.com/example/index              ,      ,                   ",
        "http://www.example.com/example/index.js           ,      ,                   ",
        "http://www.example.com/example/index.html         ,      ,                   "
    )
    fun testParsingExternalUrls(original: String, id: String?, query: String?) {
        val ref = OrchidReference.fromUrl(context, "", original)

        Clog.getInstance().setMinPriority(Clog.Priority.VERBOSE)

        expectThat(ref) {
            get { this.toString() }.isEqualTo(original)
            get { this.id }.isEqualTo(id)
            get { this.query }.isEqualTo(query)
        }
    }

}
