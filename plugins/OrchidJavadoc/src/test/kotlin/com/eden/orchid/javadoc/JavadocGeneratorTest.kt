package com.eden.orchid.javadoc

import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat


@DisplayName("Tests page-rendering behavior of Javadoc generator")
class JavadocGeneratorTest : OrchidIntegrationTest(JavadocModule()) {

    @Test
    @DisplayName("Java files are parsed, and pages are generated for each class and package.")
    fun test01() {
        configObject("javadoc", """{ "sourceDirs": "mockJava" }""")

        expectThat(execute())
            .pageWasRendered("/com/eden/orchid/mock/JavaClass1/index.html")
            .pageWasRendered("/com/eden/orchid/mock/JavaClass2/index.html")
            .pageWasRendered("/com/eden/orchid/mock/index.html")
    }

}