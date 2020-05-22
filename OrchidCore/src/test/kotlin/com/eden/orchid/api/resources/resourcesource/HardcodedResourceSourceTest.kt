package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.FileResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.strikt.asExpected
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.readToString
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mockito.mock
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.all
import strikt.assertions.containsExactly
import strikt.assertions.containsExactlyInAnyOrder
import strikt.assertions.hasSize
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.map
import strikt.assertions.size

class HardcodedResourceSourceTest {

    val underTest by lazy {
        HardcodedResourceSource(
            listOf<(OrchidContext) -> OrchidResource>(
                { createStringResource(it, "res.md") },
                { createStringResource(it, "1/res.md") },
                { createStringResource(it, "1/2/res.md") },
                { createStringResource(it, "1/2/3/res.md") }
            ),
            0,
            LocalResourceSource
        )
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
            .isA<StringResource>()
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
    fun testGetResourceEntries(input: String, recursive: Boolean, expectedFilenames: String) {
        underTest
            .getResourceEntries(context, input, null, recursive)
            .asExpected()
            .all { isA<StringResource>() }
            .map { it.getContentStream().readToString()?.trim() }
            .containsExactlyInAnyOrder(expectedFilenames.split(",").map { it.trim() })
    }


    companion object {
        fun createStringResource(context: OrchidContext, name: String, content: String = name): OrchidResource {
            return StringResource(
                OrchidReference(context, name),
                content
            )
        }
    }

}
