package com.eden.orchid.api.resources.resourcesource

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resourcesource.HardcodedResourceSourceTest.Companion.createStringResource
import com.eden.orchid.api.theme.Theme
import com.eden.orchid.strikt.asExpected
import com.eden.orchid.testhelpers.OrchidUnitTest
import com.eden.orchid.utilities.readToString
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isNull

class DefaultTemplateResourceSourceTest : OrchidUnitTest {

    val testDelegate: OrchidResourceSource by lazy {
        HardcodedResourceSource(
            listOf<(OrchidContext) -> OrchidResource>(
                { createStringResource(it, "res1.peb") },
                { createStringResource(it, "templates/res2.peb") },
                { createStringResource(it, "templates/res3.md") },
                { createStringResource(it, "templates/layouts/res4.peb") },
                { createStringResource(it, "templates/layouts/res5.md") },
                { createStringResource(it, "templates/pages/res6.peb") },
                { createStringResource(it, "templates/pages/res7.md") },
                { createStringResource(it, "templates/components/res8.peb") },
                { createStringResource(it, "templates/components/res9.md") },
                { createStringResource(it, "templates/tags/res10.peb") },
                { createStringResource(it, "templates/tags/res11.md") }
            ),
            0,
            LocalResourceSource
        )
    }
    val theme: Theme by lazy {
        mock(Theme::class.java).also {
            `when`(it.preferredTemplateExtension).thenReturn("peb")
        }
    }
    val context: OrchidContext by lazy {
        mock(OrchidContext::class.java).also {
            `when`(it.defaultTemplateExtension).thenReturn("md")
        }
    }
    val underTest: TemplateResourceSource by lazy {
        testDelegate.useForTemplates(theme)
    }

    @ParameterizedTest
    @CsvSource(
        "res1,     true,  ''                   ",
        "res2,     false, 'templates/res2.peb' ",
        "res2.md,  true,  ''                   ",
        "res2.peb, false, 'templates/res2.peb' ",
        "res3,     false, 'templates/res3.md'  ",
        "res3.md,  false, 'templates/res3.md'  ",
        "res3.peb, true,  ''                   ",

        "?res1,     true,  ''                  ",
        "?res2,     false, 'templates/res2.peb' ",
        "?res2.md,  true,  ''                   ",
        "?res2.peb, false, 'templates/res2.peb' ",
        "?res3,     false, 'templates/res3.md'  ",
        "?res3.md,  false, 'templates/res3.md'  ",
        "?res3.peb, true,  ''                   ",

        "'res1,res3', false, 'templates/res3.md'"
    )
    fun testSingleGetResourceEntry(input: String, expectNullResource: Boolean, expectedContents: String) {
        underTest
            .getResourceEntry(context, input)
            .asExpected()
            .and {
                if(expectNullResource) {
                    isNull()
                }
                else {
                    isNotNull()
                        .get { getContentStream().readToString()?.trim() }
                        .isEqualTo(expectedContents)
                }
            }
    }

    @ParameterizedTest
    @CsvSource(
        "'', res1,     true,  ''                   ",
        "'', res2,     false, 'templates/res2.peb' ",
        "'', res2.md,  true,  ''                   ",
        "'', res2.peb, false, 'templates/res2.peb' ",
        "'', res3,     false, 'templates/res3.md'  ",
        "'', res3.md,  false, 'templates/res3.md'  ",
        "'', res3.peb, true,  ''                   ",

        "'', ?res1,     true,  ''                  ",
        "'', ?res2,     false, 'templates/res2.peb' ",
        "'', ?res2.md,  true,  ''                   ",
        "'', ?res2.peb, false, 'templates/res2.peb' ",
        "'', ?res3,     false, 'templates/res3.md'  ",
        "'', ?res3.md,  false, 'templates/res3.md'  ",
        "'', ?res3.peb, true,  ''                   ",

        "'', 'res1,res3', true, ''",

        "pages, 'res3', false, 'templates/res3.md'",
        "pages, 'res6', false, 'templates/pages/res6.peb'"
    )
    fun testSubdirGetResourceEntry(templateSubdir: String, input: String, expectNullResource: Boolean, expectedContents: String) {
        underTest
            .getResourceEntry(context, templateSubdir, listOf(input))
            .asExpected()
            .and {
                if(expectNullResource) {
                    isNull()
                }
                else {
                    isNotNull()
                        .get { getContentStream().readToString()?.trim() }
                        .isEqualTo(expectedContents)
                }
            }
    }

}
