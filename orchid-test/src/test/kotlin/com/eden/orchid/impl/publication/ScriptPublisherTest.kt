package com.eden.orchid.impl.publication

import com.eden.orchid.strikt.nothingElseRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.contains
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ScriptPublisherTest : OrchidIntegrationTest() {

    var originalOutputStream: PrintStream? = null
    var testOutputStreamContent: ByteArrayOutputStream? = null

    @BeforeEach
    internal fun setUp() {
        enableLogging()
        originalOutputStream = System.out
        testOutputStreamContent = ByteArrayOutputStream()
        System.setOut(PrintStream(testOutputStreamContent!!))
    }

    @AfterEach
    internal fun tearDown() {
        disableLogging()
        System.setOut(originalOutputStream)
        originalOutputStream = null
        testOutputStreamContent = null
    }

    @Test
    fun test01() {
        flag("task", "deploy")
        flag("src", ".")
        configObject(
            "services",
            """
            |{
            |    "publications": {
            |        "stages": [
            |            {
            |                "type": "script",
            |                "command": [
            |                    "echo",
            |                    "'script publisher message'"
            |                ]
            |            }
            |        ]
            |    }
            |}
            """.trimMargin()
        )

        expectThat(execute())
            .nothingElseRendered()

        expectThat(
            testOutputStreamContent.toString().lines()
        ).contains("Script Publisher: 'script publisher message'")
    }
}
