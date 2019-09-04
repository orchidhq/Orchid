package com.eden.orchid.sourcedoc

import com.eden.orchid.groovydoc.NewGroovydocGenerator
import com.eden.orchid.javadoc.NewJavadocGenerator
import com.eden.orchid.kotlindoc.NewKotlindocGenerator
import com.eden.orchid.strikt.asExpected
import com.eden.orchid.strikt.assertWhen
import com.eden.orchid.strikt.nothingElseRendered
import com.eden.orchid.swiftdoc.NewSwiftdocGenerator
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS

class SourcedocTest : OrchidIntegrationTest(SourceDocModule()) {

    val modules = listOf("one", "two")

    @BeforeEach
    internal fun setUp() {
//        serveOn(8080)
        testCss()
        testPageStructure()
        addPageMenus()
    }

// Tests
//----------------------------------------------------------------------------------------------------------------------

    @Test
    fun `Single-module Java`() {
        javadocSetup()
        execute(withGenerator<NewJavadocGenerator>())
            .asExpected()
            .withDefaultSourcedocPages()
            .assertJava()
            .nothingElseRendered()
    }

    @Test
    fun `Multi-module Java`() {
        javadocSetup(modules)
        execute(withGenerator<NewJavadocGenerator>())
            .asExpected()
            .withDefaultSourcedocPages()
            .assertJava(modules)
            .nothingElseRendered()
    }

    @Test
    fun `Single-module Groovy`() {
        groovydocSetup()
        execute(withGenerator<NewGroovydocGenerator>())
            .asExpected()
            .withDefaultSourcedocPages()
            .assertGroovy()
            .nothingElseRendered()
    }

    @Test
    fun `Multi-module Groovy`() {
        groovydocSetup(modules)
        execute(withGenerator<NewGroovydocGenerator>())
            .asExpected()
            .withDefaultSourcedocPages()
            .assertGroovy(modules)
            .nothingElseRendered()
    }

    @Test
    fun `Single-module Kotlin`() {
        kotlindocSetup()
        execute(withGenerator<NewKotlindocGenerator>())
            .asExpected()
            .withDefaultSourcedocPages()
            .assertKotlin()
            .nothingElseRendered()
    }

    @Test
    fun `Multi-module Kotlin`() {
        kotlindocSetup(modules)
        execute(withGenerator<NewKotlindocGenerator>())
            .asExpected()
            .withDefaultSourcedocPages()
            .assertKotlin(modules)
            .nothingElseRendered()
    }

    @Test
    @EnabledOnOs(OS.MAC)
    fun `Single-module Swift`() {
        swiftdocSetup()
        execute(withGenerator<NewSwiftdocGenerator>())
            .asExpected()
            .withDefaultSourcedocPages()
            .assertSwift()
            .nothingElseRendered()
    }

    @Test
    @EnabledOnOs(OS.MAC)
    fun `Multi-module Swift`() {
        swiftdocSetup(modules)
        execute(withGenerator<NewSwiftdocGenerator>())
            .asExpected()
            .withDefaultSourcedocPages()
            .assertSwift(modules)
            .nothingElseRendered()
    }

    @Test
    fun `Single-module all kinds`() {
        javadocSetup()
        groovydocSetup()
        kotlindocSetup()
        if (OS.MAC.isCurrentOs) {
            swiftdocSetup()
        }
        execute(
            *mutableListOf(
                withGenerator<NewJavadocGenerator>(),
                withGenerator<NewGroovydocGenerator>(),
                withGenerator<NewKotlindocGenerator>()
            )
                .addWhen(OS.MAC.isCurrentOs) { withGenerator<NewSwiftdocGenerator>() }
                .toTypedArray()
        )
            .asExpected()
            .withDefaultSourcedocPages()
            .assertJava()
            .assertGroovy()
            .assertKotlin()
            .assertWhen(OS.MAC.isCurrentOs) { assertSwift() }
            .nothingElseRendered()
    }

    @Test
    fun `Multi-module all kinds`() {
        val generators = mutableListOf(
            withGenerator<NewJavadocGenerator>(),
            withGenerator<NewGroovydocGenerator>(),
            withGenerator<NewKotlindocGenerator>()
        ).addWhen(OS.MAC.isCurrentOs) { withGenerator<NewSwiftdocGenerator>() }

        javadocSetup(modules)
        groovydocSetup(modules)
        kotlindocSetup(modules)
        if (OS.MAC.isCurrentOs) {
            swiftdocSetup(modules)
        }

        execute(*generators.toTypedArray())
            .asExpected()
            .withDefaultSourcedocPages()
            .assertJava(modules)
            .assertGroovy(modules)
            .assertKotlin(modules)
            .assertWhen(OS.MAC.isCurrentOs) {
                assertSwift(modules)
            }
            .nothingElseRendered()
    }
}

fun <T> MutableList<T>.addWhen(condition: Boolean, block: () -> T): MutableList<T> {
    if (condition) {
        add(block())
    }
    return this
}
