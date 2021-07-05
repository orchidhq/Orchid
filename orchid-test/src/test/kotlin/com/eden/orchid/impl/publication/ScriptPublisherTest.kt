package com.eden.orchid.impl.publication

import clog.Clog
import clog.api.ClogLogger
import clog.dsl.addLogger
import clog.dsl.removeLogger
import com.eden.orchid.strikt.nothingElseRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isTrue

class ScriptPublisherTest : OrchidIntegrationTest(), ClogLogger {

    var scriptPublisherLogged = false

    @BeforeEach
    internal fun setUp() {
        scriptPublisherLogged = false

        enableLogging()
        Clog.addLogger(this)
    }

    @AfterEach
    internal fun tearDown() {
        disableLogging()
        // restore current Clog profile
        Clog.removeLogger(this)
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

        expectThat(scriptPublisherLogged).isTrue()
    }

// Clog Logger implementation
// ---------------------------------------------------------------------------------------------------------------------

    override fun log(priority: Clog.Priority, tag: String?, message: String) {
        if (tag == "Script Publisher") {
            scriptPublisherLogged = true
        }
    }

    override fun logException(priority: Clog.Priority, tag: String?, throwable: Throwable) {
        if (tag == "Script Publisher") {
            scriptPublisherLogged = true
        }
    }
}
