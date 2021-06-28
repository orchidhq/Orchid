package com.eden.orchid.netlify.site

import clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.tasks.OrchidTask
import com.eden.orchid.api.tasks.TaskService
import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.impl.tasks.BuildTask
import com.eden.orchid.netlify.NetlifyModule
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import kotlinx.html.p
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

class NetlifyBaseUrlTest : OrchidIntegrationTest(withGenerator<HomepageGenerator>(), NetlifyModule()) {

    @BeforeEach
    fun setUp() {
        resource(
            "homepage.md",
            """
            |---
            |---
            |baseUrl: {{ baseUrl() }}
            """.trimMargin()
        )
    }

// Ensure default url formats still work when also using Netlify URL factory
//----------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Test rending URLs using test-default base URL")
    fun test01() {
        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p { +"baseUrl: http://orchid.test/" }
                }
            }
    }

    @Test
    @DisplayName("Test rending URLs using a full URL with no extra path as the base URL")
    fun test02() {
        flag("baseUrl", "http://www.example.com")

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p { +"baseUrl: http://www.example.com/" }
                }
            }
    }

    @Test
    @DisplayName("Test rending URLs using just a slash as the base URL")
    fun test03() {
        configObject(
            "site",
            """
            |{
            |   "baseUrl": "http://www.example.com"
            |}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p { +"baseUrl: http://www.example.com/" }
                }
            }
    }

    @Test
    @DisplayName("Test rending URLs using just a slash as the base URL")
    fun test04() {
        configObject(
            "site",
            """
            |{
            |   "baseUrl": {
            |       "default": "http://www.example.com"
            |   }
            |}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p { +"baseUrl: http://www.example.com/" }
                }
            }
    }

    @Test
    @DisplayName("Test rending URLs using a full URL with an extra path as the base URL")
    fun test05() {
        configObject(
            "site",
            """
            |{
            |   "baseUrl": {
            |       "default": "http://www.example.com"
            |   }
            |}
            """.trimMargin()
        )

        val fakeServeTask = object : OrchidTask("testServe", TaskService.TaskType.SERVE, 1000) {
            override fun run(context: OrchidContext) {
                Clog.d("Running testServe task!")
                BuildTask().run(context)
            }
        }
        val testModule = object : OrchidModule() {
            override fun configure() {
                super.configure()
                addToSet(OrchidTask::class.java, fakeServeTask)
            }
        }

        flag("task", "testServe")

        expectThat(execute(testModule))
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p { +"baseUrl: http://localhost:8080/" }
                }
            }
    }

// Test Netlify base URL factory is working properly
//----------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Test rending URLs using a default Netlify URL")
    fun test11() {
        flag("environment", "prod")
        flag("logLevel", "ERROR")
        flag("NETLIFY", "true")

        flag("DEPLOY_PRIME_URL", "http://www.example.com/branch-deploy")
        flag("DEPLOY_URL", "http://www.example.com/deploy")
        flag("URL", "http://www.example.com")

        flag("CONTEXT", "branch-deploy")

        configObject(
            "site",
            """
            |{
            |   "baseUrl": [
            |       "netlify"
            |   ]
            |}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p { +"baseUrl: http://www.example.com/branch-deploy/" }
                }
            }
    }

    @Test
    @DisplayName("Test rending URLs using a default Netlify URL")
    fun test12() {
        flag("environment", "prod")
        flag("logLevel", "ERROR")
        flag("NETLIFY", "true")

        flag("DEPLOY_PRIME_URL", "http://www.example.com/branch-deploy")
        flag("DEPLOY_URL", "http://www.example.com/deploy")
        flag("URL", "http://www.example.com")

        flag("PULL_REQUEST", "true")

        configObject(
            "site",
            """
            |{
            |   "baseUrl": [
            |       "netlify"
            |   ]
            |}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p { +"baseUrl: http://www.example.com/deploy/" }
                }
            }
    }

    @Test
    @DisplayName("Test rending URLs using a default Netlify URL")
    fun test13() {
        flag("environment", "prod")
        flag("logLevel", "ERROR")
        flag("NETLIFY", "true")

        flag("DEPLOY_PRIME_URL", "http://www.example.com/branch-deploy")
        flag("DEPLOY_URL", "http://www.example.com/deploy")
        flag("URL", "http://www.example.com")

        configObject(
            "site",
            """
            |{
            |   "baseUrl": [
            |       "netlify"
            |   ]
            |}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p { +"baseUrl: http://www.example.com/" }
                }
            }
    }

    @Test
    @DisplayName("Test rending URLs using a default Netlify URL")
    fun test14() {
        flag("environment", "prod")
        flag("logLevel", "ERROR")
        flag("NETLIFY", "true")

        flag("DEPLOY_PRIME_URL", "http://www.example.com/branch-deploy")
        flag("DEPLOY_URL", "http://www.example.com/deploy")
        flag("URL", "http://www.example.com")

        configObject(
            "site",
            """
            |{
            |   "baseUrl": {
            |       "netlify": {}
            |   }
            |}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p { +"baseUrl: http://www.example.com/" }
                }
            }
    }

    @Test
    @DisplayName("Test rending URLs using a default Netlify URL")
    fun test15() {
        flag("environment", "prod")
        flag("logLevel", "ERROR")
        flag("NETLIFY", "true")

        flag("DEPLOY_PRIME_URL", "http://www.example.com/branch-deploy")
        flag("DEPLOY_URL", "http://www.example.com/deploy")
        flag("URL", "http://www.example.com")

        configObject(
            "site",
            """
            |{
            |   "baseUrl": "netlify"
            |}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p { +"baseUrl: http://www.example.com/" }
                }
            }
    }
}
