package com.eden.orchid.impl.publication

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.strikt.nothingElseRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class ScriptPublisherTest : OrchidIntegrationTest() {

    @Test
    fun test01() {
        enableLogging()
        Clog.getInstance().addTagToWhitelist("Script Publisher")

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

        expectThat(Clog.getInstance()) {
            get { lastTag }.isEqualTo("Script Publisher")
            get { lastLog }.isEqualTo("'script publisher message'")
        }
    }

}