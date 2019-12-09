package com.eden.orchid.javadoc

import com.eden.orchid.sourcedoc.SourceDocModule
import com.eden.orchid.strikt.nothingElseRendered
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat


@DisplayName("Tests page-rendering behavior of Javadoc generator")
class NewJavadocGeneratorTest : OrchidIntegrationTest(JavadocModule(), SourceDocModule()) {

    @Test
    @DisplayName("Java files are parsed, and pages are generated for each class and package.")
    fun test01() {
        flag("experimentalSourceDoc", "true")
        configObject(
            "javadoc",
            """
            |{
            |    "sourceDirs": "mockJava"
            |}
            |""".trimMargin()
        )

        expectThat(execute())
            // Module readme
            .pageWasRendered("/javadoc/index.html") { }

            // Java sources
            .pageWasRendered("/javadoc/com/eden/orchid/mock/javaannotation/index.html") { }
            .pageWasRendered("/javadoc/com/eden/orchid/mock/javaclass/index.html") { }
            .pageWasRendered("/javadoc/com/eden/orchid/mock/javaenumclass/index.html") { }
            .pageWasRendered("/javadoc/com/eden/orchid/mock/javaexceptionclass/index.html") { }
            .pageWasRendered("/javadoc/com/eden/orchid/mock/javainterface/index.html") { }
            .pageWasRendered("/javadoc/com/eden/orchid/mock/index.html") { }

            // other
            .pageWasRendered("/assets/css/orchidSourceDoc.css")
            .pageWasRendered("/favicon.ico")
            .nothingElseRendered()
    }

}
