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
            .pageWasRendered("/groovydoc/com/eden/orchid/mock/groovyannotation/index.html") { }
            .pageWasRendered("/groovydoc/com/eden/orchid/mock/groovyclass/index.html") { }
            .pageWasRendered("/groovydoc/com/eden/orchid/mock/groovyenumclass/index.html") { }
            .pageWasRendered("/groovydoc/com/eden/orchid/mock/groovyexceptionclass/index.html") { }
            .pageWasRendered("/groovydoc/com/eden/orchid/mock/groovyinterface/index.html") { }
            .pageWasRendered("/groovydoc/com/eden/orchid/mock/groovytrait/index.html") { }

            // Java sources
            .pageWasRendered("/groovydoc/com/eden/orchid/mock/javaannotation/index.html") { }
            .pageWasRendered("/groovydoc/com/eden/orchid/mock/javaclass/index.html") { }
            .pageWasRendered("/groovydoc/com/eden/orchid/mock/javaenumclass/index.html") { }
            .pageWasRendered("/groovydoc/com/eden/orchid/mock/javaexceptionclass/index.html") { }
            .pageWasRendered("/groovydoc/com/eden/orchid/mock/javainterface/index.html") { }
            .pageWasRendered("/groovydoc/com/eden/orchid/mock/index.html") { }

            // other
            .pageWasRendered("/assets/css/orchidSourceDoc.css")
            .pageWasRendered("/favicon.ico")
            .nothingElseRendered()
    }

}
