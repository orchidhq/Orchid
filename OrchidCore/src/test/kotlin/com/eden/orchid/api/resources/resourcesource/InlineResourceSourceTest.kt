package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.FileResource
import com.eden.orchid.api.resources.resource.InlineResource
import com.eden.orchid.strikt.asExpected
import com.eden.orchid.utilities.readToString
import org.junit.jupiter.api.Test
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
import java.io.File

class InlineResourceSourceTest {

    val underTest by lazy {
        InlineResourceSource(0, LocalResourceSource)
    }
    val context: OrchidContext by lazy {
        mock(OrchidContext::class.java)
    }

    @ParameterizedTest
    @CsvSource(
        "inline:res.md:res.md,             'res.md'      ",
        "inline:1/res.md:1/res.md,         '1/res.md'    ",
        "inline:1/2/res.md:1/2/res.md,     '1/2/res.md'  ",
        "inline:1/2/3/res.md:1/2/3/res.md, '1/2/3/res.md'"
    )
    fun testGetResourceEntry(input: String, expectedContents: String) {
        underTest
            .getResourceEntry(context, input)
            .asExpected()
            .isNotNull()
            .isA<InlineResource>()
            .get { getContentStream().readToString()?.trim() }
            .isEqualTo(expectedContents)
    }

}
