package com.eden.orchid.groovydoc

import com.eden.orchid.strikt.nothingElseRendered
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

@DisplayName("Tests page-rendering behavior of Groovydoc generator")
class LegacyGroovydocGeneratorTest : OrchidIntegrationTest(GroovydocModule()) {

    @Test
    @DisplayName("Groovy files are parsed, and pages are generated for each class and package.")
    fun test01() {
        configObject(
            "groovydoc",
            """
            |{
            |    "sourceDirs": [
            |        "mockGroovy",
            |        "./../../OrchidJavadoc/src/mockJava",
            |    ],
            |    "pages": {
            |        "extraCss": [
            |            "assets/css/orchidGroovydoc.scss"
            |        ]
            |    }
            |}
            |""".trimMargin()
        )

        expectThat(execute())
            // groovy sources
            .pageWasRendered("/com/eden/orchid/mock/GroovyAnnotation/index.html")
            .pageWasRendered("/com/eden/orchid/mock/GroovyClass/index.html")
            .pageWasRendered("/com/eden/orchid/mock/GroovyEnumClass/index.html")
            .pageWasRendered("/com/eden/orchid/mock/GroovyExceptionClass/index.html")
            .pageWasRendered("/com/eden/orchid/mock/GroovyInterface/index.html")
            .pageWasRendered("/com/eden/orchid/mock/GroovyTrait/index.html")
            .pageWasRendered("/com/eden/orchid/mock/index.html")

            // java sources
            .pageWasRendered("/com/eden/orchid/mock/JavaAnnotation/index.html")
            .pageWasRendered("/com/eden/orchid/mock/JavaClass/index.html")
            .pageWasRendered("/com/eden/orchid/mock/JavaEnumClass/index.html")
            .pageWasRendered("/com/eden/orchid/mock/JavaExceptionClass/index.html")
            .pageWasRendered("/com/eden/orchid/mock/JavaInterface/index.html")
            .pageWasRendered("/com/eden/orchid/mock/index.html")

            // other
            .pageWasRendered("/assets/css/orchidGroovydoc.css")
            .pageWasRendered("/favicon.ico")
            .nothingElseRendered()
    }

}
