package com.eden.orchid.groovydoc

import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

@DisplayName("Tests page-rendering behavior of Groovydoc generator")
class GroovydocGeneratorTest : OrchidIntegrationTest(GroovydocModule()) {

    @Test
    @DisplayName("Groovy files are parsed, and pages are generated for each class and package.")
    fun test01() {
        configObject("groovydoc", """{"sourceDirs": "mockGroovy" }""")

        val testResults = execute()
        expectThat(testResults).pageWasRendered("/com/eden/orchid/mock/JavaClass1/index.html")
        expectThat(testResults).pageWasRendered("/com/eden/orchid/mock/JavaClass2/index.html")
        expectThat(testResults).pageWasRendered("/com/eden/orchid/mock/GroovyClass1/index.html")
        expectThat(testResults).pageWasRendered("/com/eden/orchid/mock/GroovyClass2/index.html")
        expectThat(testResults).pageWasRendered("/com/eden/orchid/mock/index.html")
    }

}
