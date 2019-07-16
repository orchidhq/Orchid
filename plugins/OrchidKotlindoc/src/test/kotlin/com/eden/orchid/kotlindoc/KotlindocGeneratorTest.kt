package com.eden.orchid.kotlindoc

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
        configObject("kotlindoc", """{"sourceDirs": "mockKotlin" }""")

        val testResults = execute()
        expectThat(testResults).pageWasRendered("/com/eden/orchid/mock/KotlinClass1/index.html")
        expectThat(testResults).pageWasRendered("/com/eden/orchid/mock/KotlinClass2/index.html")
        expectThat(testResults).pageWasRendered("/com/eden/orchid/mock/JavaClass1/index.html")
        expectThat(testResults).pageWasRendered("/com/eden/orchid/mock/JavaClass2/index.html")
        expectThat(testResults).pageWasRendered("/com/eden/orchid/mock/index.html")
    }

}
