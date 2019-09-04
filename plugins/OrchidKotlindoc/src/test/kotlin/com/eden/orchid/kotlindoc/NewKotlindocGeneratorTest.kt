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
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/CustomString/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/JavaAnnotation/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/JavaClass/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/JavaEnumClass/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/JavaExceptionClass/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/JavaInterface/index.html") { }

            // Kotlin sources
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/KotlinAnnotation/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/KotlinClass/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/KotlinClassWithCompanionObject/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/KotlinEnumClass/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/KotlinExceptionClass/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/KotlinInlineClass/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/KotlinInterface/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/KotlinObjectClass/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/KotlinSealedClass1/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/KotlinSealedClass2/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/KotlinSealedClass3/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/KotlinSealedClasses/index.html") { }
            .pageWasRendered("/kotlindoc/com/eden/orchid/mock/index.html") { }

            // other
            .pageWasRendered("/assets/css/orchidSourceDoc.css")
            .pageWasRendered("/favicon.ico")
            .nothingElseRendered()
    }

}
