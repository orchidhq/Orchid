package com.eden.orchid.impl.site

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.tasks.OrchidTask
import com.eden.orchid.api.tasks.TaskService
import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.impl.tasks.BuildTask
import com.eden.orchid.pages.PagesModule
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import kotlinx.html.p
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

class BaseUrlTest : OrchidIntegrationTest(withGenerator<HomepageGenerator>(), PagesModule()) {

    @BeforeEach
    fun setUp() {
        enableLogging()
        flag("diagnose", true)

        resource(
            "homepage.md",
            """
            |---
            |---
            |baseUrl: {{ baseUrl() }}
            |
            |baseUrl with path: {{ baseUrl('/wiki') }}
            |
            |Page One Path: {{ link('Page One') }}
            |
            |Page Two Path: {{ link('Page Two') }}
            """.trimMargin()
        )

        resource(
            "pages/page-one.peb",
            """
            |---
            |---
            |
            |# Page One
            """.trimMargin()
        )

        resource(
            "pages/inner/page/page-two.peb",
            """
            |---
            |---
            |
            |# Page Two
            """.trimMargin()
        )
    }

// Test using base URLs set from CLI flag
//----------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Test rending URLs using test-default base URL")
    fun test01() {
        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p { +"baseUrl: http://orchid.test/" }
                    p { +"baseUrl with path: http://orchid.test/wiki" }
                    p { +"Page One Path: http://orchid.test/page-one" }
                    p { +"Page Two Path: http://orchid.test/inner/page/page-two" }
                }
            }
    }

    @Test
    @DisplayName("Test rending URLs using just a slash as the base URL")
    fun test02() {
        flag("baseUrl", "/")

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p { +"baseUrl: /" }
                    p { +"baseUrl with path: /wiki" }
                    p { +"Page One Path: /page-one" }
                    p { +"Page Two Path: /inner/page/page-two" }
                }
            }
    }

    @Test
    @DisplayName("Test rending URLs using just a path as the base URL")
    fun test03() {
        flag("baseUrl", "/some/extra/path/")

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p { +"baseUrl: /some/extra/path/" }
                    p { +"baseUrl with path: /some/extra/path/wiki" }
                    p { +"Page One Path: /some/extra/path/page-one" }
                    p { +"Page Two Path: /some/extra/path/inner/page/page-two" }
                }
            }
    }

    @Test
    @DisplayName("Test rending URLs using a full URL with no extra path as the base URL")
    fun test04() {
        flag("baseUrl", "http://www.example.com")

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p { +"baseUrl: http://www.example.com/" }
                    p { +"baseUrl with path: http://www.example.com/wiki" }
                    p { +"Page One Path: http://www.example.com/page-one" }
                    p { +"Page Two Path: http://www.example.com/inner/page/page-two" }
                }
            }
    }

    @Test
    @DisplayName("Test rending URLs using a full URL with an extra path as the base URL")
    fun test05() {
        flag("baseUrl", "http://www.example.com/some/extra/path/")

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p { +"baseUrl: http://www.example.com/some/extra/path/" }
                    p { +"baseUrl with path: http://www.example.com/some/extra/path/wiki" }
                    p { +"Page One Path: http://www.example.com/some/extra/path/page-one" }
                    p { +"Page Two Path: http://www.example.com/some/extra/path/inner/page/page-two" }
                }
            }
    }

    @Test
    @DisplayName("Test rending URLs using a full URL with an extra path as the base URL")
    fun test06() {
        flag("baseUrl", "http://www.example.com/some/extra/path/")

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
                    p { +"baseUrl with path: http://localhost:8080/wiki" }
                    p { +"Page One Path: http://localhost:8080/page-one" }
                    p { +"Page Two Path: http://localhost:8080/inner/page/page-two" }
                }
            }
    }

// Test using base URLs set as a String from config.yml
//----------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Test rending URLs using test-default base URL")
    fun test11() {
        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p { +"baseUrl: http://orchid.test/" }
                    p { +"baseUrl with path: http://orchid.test/wiki" }
                    p { +"Page One Path: http://orchid.test/page-one" }
                    p { +"Page Two Path: http://orchid.test/inner/page/page-two" }
                }
            }
    }

    @Test
    @DisplayName("Test rending URLs using just a slash as the base URL")
    fun test12() {
        configObject(
            "site",
            """
            |{
            |   "baseUrl": "/"
            |}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p { +"baseUrl: /" }
                    p { +"baseUrl with path: /wiki" }
                    p { +"Page One Path: /page-one" }
                    p { +"Page Two Path: /inner/page/page-two" }
                }
            }
    }

    @Test
    @DisplayName("Test rending URLs using just a path as the base URL")
    fun test13() {
        configObject(
            "site",
            """
            |{
            |   "baseUrl": "/some/extra/path/"
            |}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p { +"baseUrl: /some/extra/path/" }
                    p { +"baseUrl with path: /some/extra/path/wiki" }
                    p { +"Page One Path: /some/extra/path/page-one" }
                    p { +"Page Two Path: /some/extra/path/inner/page/page-two" }
                }
            }
    }

    @Test
    @DisplayName("Test rending URLs using a full URL with no extra path as the base URL")
    fun test14() {
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
                    p { +"baseUrl with path: http://www.example.com/wiki" }
                    p { +"Page One Path: http://www.example.com/page-one" }
                    p { +"Page Two Path: http://www.example.com/inner/page/page-two" }
                }
            }
    }

    @Test
    @DisplayName("Test rending URLs using a full URL with an extra path as the base URL")
    fun test15() {
        configObject(
            "site",
            """
            |{
            |   "baseUrl": "http://www.example.com/some/extra/path/"
            |}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p { +"baseUrl: http://www.example.com/some/extra/path/" }
                    p { +"baseUrl with path: http://www.example.com/some/extra/path/wiki" }
                    p { +"Page One Path: http://www.example.com/some/extra/path/page-one" }
                    p { +"Page Two Path: http://www.example.com/some/extra/path/inner/page/page-two" }
                }
            }
    }

    @Test
    @DisplayName("Test rending URLs using a full URL with an extra path as the base URL")
    fun test16() {
        configObject(
            "site",
            """
            |{
            |   "baseUrl": "http://www.example.com/some/extra/path/"
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
                    p { +"baseUrl with path: http://localhost:8080/wiki" }
                    p { +"Page One Path: http://localhost:8080/page-one" }
                    p { +"Page Two Path: http://localhost:8080/inner/page/page-two" }
                }
            }
    }
}
