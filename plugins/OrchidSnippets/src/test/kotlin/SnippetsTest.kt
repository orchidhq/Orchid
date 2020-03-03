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
        resource(
            "snippets/one.md",
            """
            |---
            |tags: 
            |  - 'tag_one'
            |  - 'default_test_snippets'
            |---
            |Content from snippet **one**
            """.trimMargin()
        )
        resource(
            "snippets/two.md",
            """
            |---
            |tags: 
            |  - 'tag_two'
            |  - 'default_test_snippets'
            |---
            |Content from snippet _two_
            """.trimMargin()
        )
    }

// End use-cases
//----------------------------------------------------------------------------------------------------------------------

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

// File Snippets Adapter
//----------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Test creating snippets from default `file` adapter config")
    fun test11() {
        resource(
            "homepage.txt",
            """
            |---
            |---
            |{{ snippet('one').content | raw }}
            """.trimMargin()
        )
        configObject(
            "snippets",
            """
            |{
            |   "sections": [
            |       {
            |           "adapter": "file",
            |           "foo": "bar"
            |       }
            |   ]
            |}
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
    @DisplayName("Test creating snippets from configured `file` adapter config")
    fun test12() {
        resource(
            "homepage.txt",
            """
            |---
            |---
            |{{ snippet('one').content | raw }}
            """.trimMargin()
        )
        resource(
            "other/snippets/dir/one.md",
            """
            |Content from other snippet **one**
            """.trimMargin()
        )

        configObject(
            "snippets",
            """
            |{
            |   "sections": [
            |       {
            |           "adapter": {
            |               "type": "file",
            |               "baseDirs": ["other"],
            |               "recursive": true
            |           },
            |           "foo": "bar"
            |       }
            |   ]
            |}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p {
                        +"Content from other snippet "
                        strong { +"one" }
                    }
                }
            }
    }

// Embedded Snippets Adapter
//----------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Test creating snippets from default `embedded` adapter config")
    fun test21() {
        resource(
            "homepage.txt",
            """
            |---
            |---
            |{{ snippet('one').content | raw }}
            """.trimMargin()
        )
        configObject(
            "snippets",
            """
            |{
            |   "sections": [
            |       {
            |           "adapter": "embedded",
            |           "foo": "bar"
            |       }
            |   ]
            |}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {

                }
            }
    }

    @Test
    @DisplayName("Test creating snippets from configured `embedded` adapter config")
    fun test22() {
        resource(
            "homepage.txt",
            """
            |---
            |---
            |{{ snippet('one').content | raw }}
            """.trimMargin()
        )
        configObject(
            "snippets",
            """
            |{
            |   "sections": [
            |       {
            |           "adapter": {
            |               "type": "embedded",
            |               "foo": "bar"
            |           },
            |           "foo": "bar"
            |       }
            |   ]
            |}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {

                }
            }
    }

// Remote Snippets Adapter
//----------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Test creating snippets from default `remote` adapter config")
    fun test31() {
        resource(
            "homepage.txt",
            """
            |---
            |---
            |{{ snippet('one').content | raw }}
            """.trimMargin()
        )
        configObject(
            "snippets",
            """
            |{
            |   "sections": [
            |       {
            |           "adapter": "remote",
            |           "foo": "bar"
            |       }
            |   ]
            |}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {

                }
            }
    }

    @Test
    @DisplayName("Test creating snippets from configured `remote` adapter config")
    fun test32() {
        resource(
            "homepage.txt",
            """
            |---
            |---
            |{{ snippet('one').content | raw }}
            """.trimMargin()
        )
        configObject(
            "snippets",
            """
            |{
            |   "sections": [
            |       {
            |           "adapter": {
            |               "type": "remote",
            |               "foo": "bar"
            |           },
            |           "foo": "bar"
            |       }
            |   ]
            |}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {

                }
            }
    }

}
