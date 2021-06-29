package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.ClasspathResource
import com.eden.orchid.strikt.asExpected
import com.eden.orchid.testhelpers.OrchidUnitTest
import com.eden.orchid.utilities.SuppressedWarnings
import com.eden.orchid.utilities.readToString
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mockito.mock
import strikt.api.expectThrows
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull

class ClasspathResourceSourceTest : OrchidUnitTest {

    val underTest by lazy {
        ClasspathResourceSource(ClasspathResourceSourceTest::class.java.classLoader, 0, LocalResourceSource)
    }
    val context: OrchidContext by lazy {
        mock(OrchidContext::class.java)
    }

    @ParameterizedTest
    @CsvSource(
        "res.md,        'res.md'      ",
        "1/res.md,      '1/res.md'    ",
        "1/2/res.md,    '1/2/res.md'  ",
        "1/2/3/res.md,  '1/2/3/res.md'",

        "/res.md,       'res.md'      ",
        "/1/res.md,     '1/res.md'    ",
        "/1/2/res.md,   '1/2/res.md'  ",
        "/1/2/3/res.md, '1/2/3/res.md'"
    )
    fun testGetResourceEntry(input: String, expectedContents: String) {
        underTest
            .getResourceEntry(context, input)
            .asExpected()
            .isNotNull()
            .isA<ClasspathResource>()
            .get { getContentStream().readToString()?.trim() }
            .isEqualTo(expectedContents)
    }

    @ParameterizedTest
    @CsvSource(
        "'',     false, 'res.md'      ",
        "1,      false, '1/res.md'    ",
        "1/2,    false, '1/2/res.md'  ",
        "1/2/3,  false, '1/2/3/res.md'",

        "/,       false, 'res.md'      ",
        "/1,      false, '1/res.md'    ",
        "/1/2,    false, '1/2/res.md'  ",
        "/1/2/3,  false, '1/2/3/res.md'",

        "1/,      false, '1/res.md'    ",
        "1/2/,    false, '1/2/res.md'  ",
        "1/2/3/,  false, '1/2/3/res.md'",

        "/,       true,  'res.md, 1/res.md, 1/2/res.md, 1/2/3/res.md'",
        "/1,      true,  '1/res.md, 1/2/res.md, 1/2/3/res.md'",
        "/1/2,    true,  '1/2/res.md, 1/2/3/res.md'",
        "/1/2/3,  true,  '1/2/3/res.md'"
    )
    @Suppress(SuppressedWarnings.UNUSED_PARAMETER)
    fun testGetResourceEntries(input: String, recursive: Boolean, expectedFilenames: String) {
        expectThrows<NotImplementedError> {
            underTest.getResourceEntries(context, "", null, false)
        }
    }
}
