package com.eden.orchid.kotlindoc

import com.eden.orchid.strikt.nothingElseRendered
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

@DisplayName("Tests page-rendering behavior of Kotlindoc generator")
class KotlindocGeneratorTest : OrchidIntegrationTest(KotlindocModule()) {

    @Test
    @DisplayName("Kotlin and Java files are parsed, and pages are generated for each class and package.")
    fun test01() {
        configObject(
            "kotlindoc",
            """
            |{
            |    "sourceDirs": [
            |        "mockKotlin",
            |        "./../../OrchidJavadoc/src/mockJava",
            |    ],
            |    "pages": {
            |        "extraCss": [
            |            "assets/css/orchidKotlindoc.scss"
            |        ]
            |    }
            |}
            |""".trimMargin()
        )

        expectThat(execute())
            // kotlin sources
            .pageWasRendered("/com/eden/orchid/mock/KotlinAnnotation/index.html")
            .pageWasRendered("/com/eden/orchid/mock/KotlinClass/index.html")
            .pageWasRendered("/com/eden/orchid/mock/KotlinClassWithCompanionObject/index.html")
            .pageWasRendered("/com/eden/orchid/mock/KotlinEnumClass/index.html")
            .pageWasRendered("/com/eden/orchid/mock/KotlinExceptionClass/index.html")
            .pageWasRendered("/com/eden/orchid/mock/KotlinInlineClass/index.html")
            .pageWasRendered("/com/eden/orchid/mock/KotlinInterface/index.html")
            .pageWasRendered("/com/eden/orchid/mock/KotlinObjectClass/index.html")
            .pageWasRendered("/com/eden/orchid/mock/KotlinSealedClasses/index.html")
            .pageWasRendered("/com/eden/orchid/mock/KotlinSealedClass1/index.html")
            .pageWasRendered("/com/eden/orchid/mock/KotlinSealedClass2/index.html")
            .pageWasRendered("/com/eden/orchid/mock/KotlinSealedClass3/index.html")
            .pageWasRendered("/com/eden/orchid/mock/CustomString/index.html")

            // java sources
            .pageWasRendered("/com/eden/orchid/mock/JavaAnnotation/index.html")
            .pageWasRendered("/com/eden/orchid/mock/JavaClass/index.html")
            .pageWasRendered("/com/eden/orchid/mock/JavaEnumClass/index.html")
            .pageWasRendered("/com/eden/orchid/mock/JavaExceptionClass/index.html")
            .pageWasRendered("/com/eden/orchid/mock/JavaInterface/index.html")
            .pageWasRendered("/com/eden/orchid/mock/index.html")

            // other
            .pageWasRendered("/assets/css/orchidKotlindoc.css")
            .pageWasRendered("/favicon.ico")
            .nothingElseRendered()
    }

}
