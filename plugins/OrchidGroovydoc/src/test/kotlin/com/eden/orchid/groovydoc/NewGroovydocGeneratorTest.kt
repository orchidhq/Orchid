package com.eden.orchid.groovydoc

import com.eden.orchid.sourcedoc.SourceDocModule
import com.eden.orchid.strikt.nothingElseRendered
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

@DisplayName("Tests page-rendering behavior of Groovydoc generator")
class NewGroovydocGeneratorTest : OrchidIntegrationTest(GroovydocModule(), SourceDocModule()) {

    @Test
    @DisplayName("Groovy files are parsed, and pages are generated for each class and package.")
    fun test01() {
        flag("experimentalSourceDoc", "true")
        configObject(
            "groovydoc",
            """
            |{
            |    "sourceDirs": [
            |        "mockGroovy",
            |        "./../../OrchidJavadoc/src/mockJava",
            |    ]
            |}
            |""".trimMargin()
        )

        expectThat(execute())

            // Module readme
            .pageWasRendered("/groovydoc/index.html") { }

            // Groovy sources
            .pageWasRendered("/groovydoc/com/eden/orchid/mock/GroovyAnnotation/index.html") { }
            .pageWasRendered("/groovydoc/com/eden/orchid/mock/GroovyClass/index.html") { }
            .pageWasRendered("/groovydoc/com/eden/orchid/mock/GroovyEnumClass/index.html") { }
            .pageWasRendered("/groovydoc/com/eden/orchid/mock/GroovyExceptionClass/index.html") { }
            .pageWasRendered("/groovydoc/com/eden/orchid/mock/GroovyInterface/index.html") { }
            .pageWasRendered("/groovydoc/com/eden/orchid/mock/GroovyTrait/index.html") { }

            // Java sources
            .pageWasRendered("/groovydoc/com/eden/orchid/mock/JavaAnnotation/index.html") { }
            .pageWasRendered("/groovydoc/com/eden/orchid/mock/JavaClass/index.html") { }
            .pageWasRendered("/groovydoc/com/eden/orchid/mock/JavaEnumClass/index.html") { }
            .pageWasRendered("/groovydoc/com/eden/orchid/mock/JavaExceptionClass/index.html") { }
            .pageWasRendered("/groovydoc/com/eden/orchid/mock/JavaInterface/index.html") { }
            .pageWasRendered("/groovydoc/com/eden/orchid/mock/index.html") { }

            // other
            .pageWasRendered("/assets/css/orchidSourceDoc.css")
            .pageWasRendered("/favicon.ico")
            .nothingElseRendered()
    }

}
