package com.eden.orchid.sourcedoc

import com.eden.orchid.testhelpers.OrchidIntegrationTest
import org.junit.jupiter.api.Test

class SourcedocTest : OrchidIntegrationTest() {

    @Test
    fun test01() {
        enableLogging()
        configObject(
            "javadoc",
            """
            |{
            |    "sourceDirs": [
            |        "./../../OrchidJavadoc/src/mockJava"
            |    ]
            |}
            |""".trimMargin()
        )
//        serveOn(8080)
        val testResults = execute(NewJavadocGeneratorModule())
        testResults.printResults()
    }

    @Test
    fun test02() {
        enableLogging()
        configObject(
            "groovydoc",
            """
            |{
            |    "sourceDirs": [
            |        "./../../OrchidJavadoc/src/mockJava",
            |        "./../../OrchidGroovydoc/src/mockGroovy",
            |    ]
            |}
            |""".trimMargin()
        )
//        serveOn(8080)
        val testResults = execute(NewGroovydocGeneratorModule())
        testResults.printResults()
    }

    @Test
    fun test03() {
        enableLogging()
        configObject(
            "kotlindoc",
            """
            |{
            |    "sourceDirs": [
            |        "./../../OrchidJavadoc/src/mockJava",
            |        "./../../OrchidKotlindoc/src/mockKotlin"
            |    ]
            |}
            |""".trimMargin()
        )
//        serveOn(8080)
        val testResults = execute(NewKotlindocGeneratorModule())
        testResults.printResults()
    }
}
