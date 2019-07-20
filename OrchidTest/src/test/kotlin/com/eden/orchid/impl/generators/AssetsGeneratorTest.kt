package com.eden.orchid.impl.generators

import com.eden.orchid.strikt.nothingElseRendered
import com.eden.orchid.strikt.pageWasNotRendered
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.strikt.printResults
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

class AssetsGeneratorTest : OrchidIntegrationTest(withGenerator<AssetsGenerator>()) {

    @BeforeEach
    internal fun setUp() {
        enableLogging()
    }

    @Test
    @DisplayName("By default, assets are loaded from `assets/media`")
    fun test01() {
        resource("assets/media/photo-one.jpg")
        resource("assets/images/photo-two.jpg")

        expectThat(execute())
            .printResults()
            .pageWasRendered("/assets/media/photo-one.jpg")
            .pageWasNotRendered("/assets/images/photo-two.jpg")
            .nothingElseRendered()
    }

    @Test
    @DisplayName("The assets directory can be changed")
    fun test02() {
        configObject("assets", """
            {
                "sourceDirs": [
                    "assets/images"
                ]
            }
        """.trimIndent())

        resource("assets/media/photo-one.jpg")
        resource("assets/images/photo-two.jpg")

        expectThat(execute())
            .printResults()
            .pageWasNotRendered("/assets/media/photo-one.jpg")
            .pageWasRendered("/assets/images/photo-two.jpg")
            .nothingElseRendered()
    }
}
