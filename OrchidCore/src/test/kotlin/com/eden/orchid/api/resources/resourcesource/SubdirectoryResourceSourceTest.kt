package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.FileResource
import com.eden.orchid.strikt.asExpected
import com.eden.orchid.testhelpers.OrchidUnitTest
import com.eden.orchid.utilities.readToString
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mockito.mock
import strikt.assertions.all
import strikt.assertions.containsExactlyInAnyOrder
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.map
import java.io.File

class SubdirectoryResourceSourceTest : OrchidUnitTest {

    val underTest by lazy {
        SubdirectoryResourceSource(
            FileResourceSource(File("./src/test/resources").toPath(), 0, LocalResourceSource),
            "1"
        )
    }
    val context: OrchidContext by lazy {
        mock(OrchidContext::class.java)
    }

    @ParameterizedTest
    @CsvSource(
        "res.md,      '1/res.md'    ",
        "2/res.md,    '1/2/res.md'  ",
        "2/3/res.md,  '1/2/3/res.md'",

        "/res.md,     '1/res.md'    ",
        "/2/res.md,   '1/2/res.md'  ",
        "/2/3/res.md, '1/2/3/res.md'"
    )
    fun testGetResourceEntry(input: String, expectedContents: String) {
        underTest
            .getResourceEntry(context, input)
            .asExpected()
            .isNotNull()
            .isA<FileResource>()
            .get { getContentStream().readToString()?.trim() }
            .isEqualTo(expectedContents)
    }

    @ParameterizedTest
    @CsvSource(
        "'',   false, '1/res.md'    ",
        "2,    false, '1/2/res.md'  ",
        "2/3,  false, '1/2/3/res.md'",

        "/2,    false, '1/2/res.md'  ",
        "/2/3,  false, '1/2/3/res.md'",

        "2/,    false, '1/2/res.md'  ",
        "2/3/,  false, '1/2/3/res.md'",

        "/,     true,  '1/res.md, 1/2/res.md, 1/2/3/res.md'",
        "/2,    true,  '1/2/res.md, 1/2/3/res.md'",
        "/2/3,  true,  '1/2/3/res.md'"
    )
    fun testGetResourceEntries(input: String, recursive: Boolean, expectedFilenames: String) {
        underTest
            .getResourceEntries(context, input, null, recursive)
            .asExpected()
            .all { isA<FileResource>() }
            .map { it.getContentStream().readToString()?.trim() }
            .containsExactlyInAnyOrder(expectedFilenames.split(",").map { it.trim() })
    }
}
