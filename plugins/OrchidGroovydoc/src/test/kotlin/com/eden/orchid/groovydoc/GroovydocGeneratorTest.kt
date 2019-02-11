package com.eden.orchid.groovydoc

import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.pageWasRendered
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

@DisplayName("Tests page-rendering behavior of Groovydoc generator")
class GroovydocGeneratorTest : OrchidIntegrationTest(GroovydocModule()) {

    @Test
    @DisplayName("Groovy files are parsed, and pages are generated for each class and package.")
    fun test01() {
        enableLogging()
        configObject("groovydoc", """{"sourceDirs": "mockGroovy" }""")
//        configObject("allPages", """{"extraCss": "assets/css/orchidGroovydoc.scss" }""")
//        serveOn(8080)

        val testResults = execute()
        expectThat(testResults).pageWasRendered("/com/eden/orchid/mock/JavaClass1/index.html")
        expectThat(testResults).pageWasRendered("/com/eden/orchid/mock/JavaClass2/index.html")
        expectThat(testResults).pageWasRendered("/com/eden/orchid/mock/GroovyClass1/index.html")
        expectThat(testResults).pageWasRendered("/com/eden/orchid/mock/GroovyClass2/index.html")
        expectThat(testResults).pageWasRendered("/com/eden/orchid/mock/index.html")
    }

}
