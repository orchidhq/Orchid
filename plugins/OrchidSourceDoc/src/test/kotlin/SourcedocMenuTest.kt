package com.eden.orchid.sourcedoc

import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SourcedocMenuTest : OrchidIntegrationTest(SourceDocModule()) {

    @BeforeEach
    internal fun setUp() {
        enableLogging()
//        serveOn(8080)
        testCss()
        testMenuStructure()
        addPageMenus()
    }

// Tests
//----------------------------------------------------------------------------------------------------------------------

    @Test
    fun test01() {
        javadocSetup()
        execute(withGenerator<NewJavadocGenerator>())
    }

    @Test
    fun test02() {
        groovydocSetup()
        execute(withGenerator<NewGroovydocGenerator>())
    }

    @Test
    fun test03() {
        kotlindocSetup()
        execute(withGenerator<NewKotlindocGenerator>())
    }

    @Test
    fun test04() {
        javadocSetup()
        groovydocSetup()
        kotlindocSetup()
        execute(
            withGenerator<NewJavadocGenerator>(),
            withGenerator<NewGroovydocGenerator>(),
            withGenerator<NewKotlindocGenerator>()
        )
    }
}
