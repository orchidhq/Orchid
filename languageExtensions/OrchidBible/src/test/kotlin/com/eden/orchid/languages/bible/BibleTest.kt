package com.eden.orchid.languages.bible

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

@DisplayName("Tests behavior of using Bible verse functions")
class BibleTest : OrchidIntegrationTest(withGenerator<HomepageGenerator>(), BibleModule()) {

    @Test
    @DisplayName("Test that the bible verse function works using the default version")
    fun test01() {
        configObject(
            "theme",
            """
            |{
            |    "metaComponents": [
            |        { 
            |            "type": "reftagger",
            |            "bibleReader": "Faithlife",
            |            "roundedCorners": true,
            |            "dropShadow": false,
            |            "darkMode": true,
            |            "excludeTags": ["h1", "h2", "h3"],
            |            "excludeClasses": [".h1", ".h2", ".h3"],
            |        }
            |    ]
            |}
            """.trimMargin()
        )

        resource(
            "homepage.md",
            """
            |- John 3:16
            |- Galatians 2:20
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html")
    }
}
