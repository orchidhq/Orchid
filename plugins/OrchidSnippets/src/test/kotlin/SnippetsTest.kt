package com.eden.orchid.snippets

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import kotlinx.html.div
import kotlinx.html.em
import kotlinx.html.p
import kotlinx.html.strong
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

class SnippetsTest : OrchidIntegrationTest(SnippetsModule(), withGenerator<HomepageGenerator>()) {

    @BeforeEach
    fun setUp() {
        enableLogging()

        resource(
            "snippets/one.md",
            """
            |Content from snippet **one**
            """.trimMargin()
        )
    }

    @Test
    @DisplayName("Test rendering a snippet with a TemplateFunction")
    fun test01() {
        resource(
            "homepage.txt",
            """
            |---
            |---
            |{{ snippet('one').content | raw }}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p {
                        +"Content from snippet "
                        strong { +"one" }
                    }
                }
            }
    }

    @Test
    @DisplayName("Test rendering a snippet with a TemplateTag")
    fun test02() {
        resource(
            "homepage.txt",
            """
            |---
            |---
            |{% snippet 'one' %}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p {
                        +"Content from snippet "
                        strong { +"one" }
                    }
                }
            }
    }

    @Test
    @DisplayName("Test rendering snippet tabs with a TemplateTag")
    fun test03() {
        resource(
            "homepage.txt",
            """
            |---
            |---
            |{% snippets ['tag_one'] %}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p {
                        +"Content from snippet "
                        strong { +"one" }
                    }
                }
            }
    }

    @Test
    @DisplayName("Test rendering a snippet with a Component")
    fun test04() {
        resource(
            "homepage.txt",
            "",
            mapOf(
                "components" to listOf<Any>(
                    mapOf<String, Any>(
                        "type" to "snippet",
                        "snippetName" to "one"
                    )
                )
            )
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    div("component component-snippet component-order-0") {
                        p {
                            +"Content from snippet "
                            strong { +"one" }
                        }
                    }
                }
            }
    }

    @Test
    @DisplayName("Test rendering snippet tabs with a Component")
    fun test05() {
        resource(
            "homepage.txt",
            "",
            mapOf(
                "components" to listOf<Any>(
                    mapOf<String, Any>(
                        "type" to "snippets",
                        "snippetTags" to listOf(
                            "tag_one"
                        )
                    )
                )
            )
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    div("component component-snippets component-order-0") {
                        p {
                            +"Content from snippet "
                            strong { +"one" }
                        }
                    }
                }
            }
    }

}
