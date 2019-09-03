package com.eden.orchid.sourcedoc

import com.eden.orchid.strikt.asExpected
import com.eden.orchid.strikt.assertWhen
import com.eden.orchid.strikt.nothingElseRendered
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
        enableLogging()
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
            .withSourcedocPages()
            .assertJava()
            .nothingElseRendered()
    }

    @Test
    fun `Multi-module Java`() {
        javadocSetup(modules)
        execute(withGenerator<NewJavadocGenerator>())
            .asExpected()
            .withSourcedocPages("java", modules)
            .assertJava(modules)
            .nothingElseRendered()
    }

    @Test
    fun `Single-module Groovy`() {
        groovydocSetup()
        execute(withGenerator<NewGroovydocGenerator>())
            .asExpected()
            .withSourcedocPages()
            .assertGroovy()
            .nothingElseRendered()
    }

    @Test
    fun `Multi-module Groovy`() {
        groovydocSetup(modules)
        execute(withGenerator<NewGroovydocGenerator>())
            .asExpected()
            .withSourcedocPages("groovy", modules)
            .assertGroovy(modules)
            .nothingElseRendered()
    }

    @Test
    fun `Single-module Kotlin`() {
        kotlindocSetup()
        execute(withGenerator<NewKotlindocGenerator>())
            .asExpected()
            .withSourcedocPages()
            .assertKotlin()
            .nothingElseRendered()
    }

    @Test
    fun `Multi-module Kotlin`() {
        kotlindocSetup(modules)
        execute(withGenerator<NewKotlindocGenerator>())
            .asExpected()
            .withSourcedocPages("kotlin", modules)
            .assertKotlin(modules)
            .nothingElseRendered()
    }

    @Test
    @EnabledOnOs(OS.MAC)
    fun `Single-module Swift`() {
        swiftdocSetup()
        execute(withGenerator<NewSwiftdocGenerator>())
            .asExpected()
            .withSourcedocPages()
            .assertSwift()
            .nothingElseRendered()
    }

    @Test
    @EnabledOnOs(OS.MAC)
    fun `Multi-module Swift`() {
        swiftdocSetup(modules)
        execute(withGenerator<NewSwiftdocGenerator>())
            .asExpected()
            .withSourcedocPages("swift", modules)
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
            .withSourcedocPages()
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
            .assertJava(modules)
            .withSourcedocPages("java", modules)
            .assertGroovy(modules)
            .withSourcedocPages("groovy", modules)
            .assertKotlin(modules)
            .withSourcedocPages("kotlin", modules)
            .assertWhen(OS.MAC.isCurrentOs) {
                assertSwift(modules)
                withSourcedocPages("swift", modules)
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
