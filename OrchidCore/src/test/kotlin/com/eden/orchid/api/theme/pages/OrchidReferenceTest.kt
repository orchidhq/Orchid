package com.eden.orchid.api.theme.pages

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.site.OrchidSite
import com.eden.orchid.api.site.OrchidSiteImpl
import com.eden.orchid.testhelpers.OrchidUnitTest
import com.eden.orchid.utilities.SuppressedWarnings
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo

class OrchidReferenceTest : OrchidUnitTest {

    lateinit var context: OrchidContext

    @BeforeEach
    fun setUp() {
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
        // || base URL         | original URL | Formatted |

        // absolute URLs
        "http://www.example.com            , example      , http://www.example.com/example",
        "http://www.example.com/           , example      , http://www.example.com/example",
        "http://www.example.com/inner      , example      , http://www.example.com/inner/example",
        "http://www.example.com/inner/     , example      , http://www.example.com/inner/example",
        "http://www.example.com/inner/deep , example      , http://www.example.com/inner/deep/example",
        "http://www.example.com/inner/deep/, example      , http://www.example.com/inner/deep/example",
        "http://www.example.com            , /example     , http://www.example.com/example",
        "http://www.example.com/           , /example     , http://www.example.com/example",
        "http://www.example.com/inner      , /example     , http://www.example.com/inner/example",
        "http://www.example.com/inner/     , /example     , http://www.example.com/inner/example",
        "http://www.example.com/inner/deep , /example     , http://www.example.com/inner/deep/example",
        "http://www.example.com/inner/deep/, /example     , http://www.example.com/inner/deep/example",

        "/                                 , example      , /example",
        "/                                 , example      , /example",
        "/inner                            , example      , /inner/example",
        "/inner/                           , example      , /inner/example",
        "/inner/deep                       , example      , /inner/deep/example",
        "/inner/deep/                      , example      , /inner/deep/example",
        "/                                 , /example     , /example",
        "/                                 , /example     , /example",
        "/inner                            , /example     , /inner/example",
        "/inner/                           , /example     , /inner/example",
        "/inner/deep                       , /example     , /inner/deep/example",
        "/inner/deep/                      , /example     , /inner/deep/example"
    )
    fun testCreatingLocalUrls(baseUrl: String, original: String, formatted: String) {
        val site = OrchidSiteImpl("1.0", "1.0", baseUrl, "dev", "peb")
        `when`(context.getService(OrchidSite::class.java)).thenReturn(site)
        `when`(context.baseUrl).thenCallRealMethod()

        val ref = OrchidReference(context, original, true)

        Clog.getInstance().setMinPriority(Clog.Priority.VERBOSE)

        expectThat(ref) {
            get { this.toString() }.isEqualTo(formatted)
        }
    }

    @ParameterizedTest
    @CsvSource(
        // | original URL                                  | path          | filename | extension | id  | query |
        // absolute URLs
        "http://www.example.com/example                    , ''            , example  , html      ,     ,                   ",
        "https://www.example.com/example                   , ''            , example  , html      ,     ,                   ",
        "https://www.example.com/example#one               , ''            , example  , html      , one ,                   ",
        "https://www.example.com/example?one=two           , ''            , example  , html      ,     , one=two           ",
        "https://www.example.com/example?one=two&three=four, ''            , example  , html      ,     , one=two&three=four",
        "http://www.example.com/example.js                 , ''            , example  , js        ,     ,                   ",
        "http://www.example.com/example/index              , example       , index    , html      ,     ,                   ",
        "http://www.example.com/example/index.js           , example       , index    , js        ,     ,                   ",
        "http://www.example.com/example/index.html         , example       , index    , html      ,     ,                   ",

        // relative URLs
        "/                                                 , ''            , index    , html      ,     ,                   ",
        "/example                                          , ''            , example  , html      ,     ,                   ",
        "/example/inner                                    , example       , inner    , html      ,     ,                   ",
        "/example/index.js                                 , example       , index    , js        ,     ,                   ",
        "/example/index.html                               , example       , index    , html      ,     ,                   "
    )
    @Suppress(SuppressedWarnings.UNUSED_PARAMETER)
    fun testParsingExternalUrls(original: String, path: String?, fileName: String?, extension: String?, id: String?, query: String?) {
        val ref = OrchidReference.fromUrl(context, "", original)

        Clog.getInstance().setMinPriority(Clog.Priority.VERBOSE)

        expectThat(ref) {
            get { this.toString() }.isEqualTo(original)
            get { this.path }.isEqualTo(path)
            get { this.id }.isEqualTo(id)
            get { this.query }.isEqualTo(query)
        }
    }

}
