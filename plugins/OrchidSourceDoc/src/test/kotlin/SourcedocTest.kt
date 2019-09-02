package com.eden.orchid.sourcedoc

import com.eden.orchid.strikt.asExpected
import com.eden.orchid.strikt.assertWhen
import com.eden.orchid.strikt.nothingElseRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import strikt.api.expectThat

class SourcedocTest : OrchidIntegrationTest(SourceDocModule()) {

    @BeforeEach
    internal fun setUp() {
        enableLogging()
//        serveOn(8080)
        testCss()
        testPageStructure()
        addPageMenus()
    }

// Tests
//----------------------------------------------------------------------------------------------------------------------

    @Test
    fun test01() {
        javadocSetup()
        execute(withGenerator<NewJavadocGenerator>())
            .asExpected()
            .withSourcedocPages()
            .assertJava()
            .nothingElseRendered()
    }

    @Test
    fun test02() {
        groovydocSetup()
        execute(withGenerator<NewGroovydocGenerator>())
            .asExpected()
            .withSourcedocPages()
            .assertGroovy()
            .nothingElseRendered()
    }

    @Test
    fun test03() {
        kotlindocSetup()
        execute(withGenerator<NewKotlindocGenerator>())
            .asExpected()
            .withSourcedocPages()
            .assertKotlin()
            .nothingElseRendered()
    }

    @Test
    @EnabledOnOs(OS.MAC)
    fun test04() {
        swiftdocSetup()
        execute(withGenerator<NewSwiftdocGenerator>())
            .asExpected()
            .withSourcedocPages()
            .assertSwift()
            .nothingElseRendered()
    }

    @Test
    @Disabled
    fun test05() {
        javadocSetup()
        groovydocSetup()
        kotlindocSetup()
        if (OS.MAC.isCurrentOs) {
            swiftdocSetup()
        }
        val results = execute(
            *mutableListOf(
                withGenerator<NewJavadocGenerator>(),
                withGenerator<NewGroovydocGenerator>(),
                withGenerator<NewKotlindocGenerator>()
            )
                .addWhen(OS.MAC.isCurrentOs) { withGenerator<NewSwiftdocGenerator>() }
                .toTypedArray()
        )

        expectThat(results)
            .assertJava()
            .assertGroovy()
            .assertKotlin()
            .assertWhen(OS.MAC.isCurrentOs) { assertSwift() }
//            .nothingElseRendered()
    }
}

fun <T> MutableList<T>.addWhen(condition: Boolean, block: () -> T): MutableList<T> {
    if (condition) {
        add(block())
    }
    return this
}
