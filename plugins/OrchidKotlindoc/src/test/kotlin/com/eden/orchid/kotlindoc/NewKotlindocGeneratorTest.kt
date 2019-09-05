package com.eden.orchid.kotlindoc

import com.eden.orchid.sourcedoc.SourceDocModule
import com.eden.orchid.strikt.nothingElseRendered
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

@DisplayName("Tests page-rendering behavior of Kotlindoc generator")
class NewKotlindocGeneratorTest : OrchidIntegrationTest(KotlindocModule(), SourceDocModule()) {

    @Test
    @DisplayName("Kotlin and Java files are parsed, and pages are generated for each class and package.")
    fun test01() {
        flag("experimentalSourceDoc", "true")
        configObject(
            "kotlindoc",
            """
            |{
            |    "sourceDirs": [
            |        "mockKotlin",
            |        "./../../OrchidJavadoc/src/mockJava",
            |    ]
            |}
            |""".trimMargin()
        )

        expectThat(execute())
            // Module readme
            .pageWasRendered("/kotlindoc/index.html") { }

            // Java sources
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/customstring/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/javaannotation/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/javaclass/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/javaenumclass/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/javaexceptionclass/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/javainterface/index.html") { }

            // Kotlin sources
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/kotlinannotation/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/kotlinclass/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/kotlinclasswithcompanionobject/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/kotlinenumclass/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/kotlinexceptionclass/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/kotlininlineclass/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/kotlininterface/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/kotlinobjectclass/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/kotlinsealedclass1/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/kotlinsealedclass2/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/kotlinsealedclass3/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/kotlinsealedclasses/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/index.html") { }

            // other
            .pageWasRendered("/assets/css/orchidSourceDoc.css")
            .pageWasRendered("/favicon.ico")
            .nothingElseRendered()
    }

}
