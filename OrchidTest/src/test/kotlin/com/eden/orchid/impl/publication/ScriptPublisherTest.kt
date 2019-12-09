package com.eden.orchid.impl.publication

import com.caseyjbrooks.clog.Clog
import com.caseyjbrooks.clog.ClogLogger
import com.caseyjbrooks.clog.ClogProfile
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

        Clog.setCurrentProfile("ScriptPublisherTest") {
            ClogProfile().apply {
                addLogger(null, this@ScriptPublisherTest)
                addLogger(Clog.KEY_V, this@ScriptPublisherTest)
                addLogger(Clog.KEY_D, this@ScriptPublisherTest)
                addLogger(Clog.KEY_I, this@ScriptPublisherTest)
                addLogger(Clog.KEY_W, this@ScriptPublisherTest)
                addLogger(Clog.KEY_E, this@ScriptPublisherTest)
                addLogger(Clog.KEY_WTF, this@ScriptPublisherTest)
            }
        }
    }

    @AfterEach
    internal fun tearDown() {
        disableLogging()
        // restore current Clog profile
        Clog.setCurrentProfile(null)
    }

    @Test
    fun test01() {
        flag("task", "deploy")
        configObject(
            "services", """
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
//----------------------------------------------------------------------------------------------------------------------

    override fun isActive(): Boolean = true

    override fun priority(): Clog.Priority = Clog.Priority.VERBOSE

    override fun log(tag: String?, message: String?): Int {
        if(tag == "Script Publisher") {
            scriptPublisherLogged = true
        }

        return 0
    }

    override fun log(tag: String?, message: String?, throwable: Throwable?): Int {
        return log(tag, message)
    }


}