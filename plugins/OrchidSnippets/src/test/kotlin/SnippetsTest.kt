package com.eden.orchid.snippets

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import kotlinx.html.br
import kotlinx.html.div
import kotlinx.html.em
import kotlinx.html.li
import kotlinx.html.ol
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
            |{{ snippet('homepage_one').content | raw }}
            |<br>
            |{{ snippet('homepage_two').content | raw }}
            """.trimMargin()
        )
        resource(
            "src/test/kotlin/fileWithSnippets.md",
            """
            |// snippet::homepage_one[]
            |  Content from snippet **one**
            |// end::homepage_one
            |
            |// snippet::homepage_two[]
            |  Content from snippet **two**
            |    plus additional content
            |// end::homepage_two
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
            |               "baseDirs": "src/test"
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
                        +"Content from snippet "
                        strong { +"one" }
                    }
                    br()
                    p {
                        +"Content from snippet "
                        strong { +"two" }
                        +"  plus additional content"
                    }
                }
            }
    }

    @Test
    @DisplayName("Test creating snippets from `embedded` adapter config, using custom tag regexes")
    fun test22() {
        resource(
            "homepage.txt",
            """
            |---
            |---
            |{{ snippet('homepage_one').content | raw }}
            |<br>
            |{{ snippet('homepage_two').content | raw }}
            """.trimMargin()
        )
        resource(
            "src/test/kotlin/fileWithSnippets.md",
            """
            |// START homepage_one
            |  Content from snippet **one**
            |// END homepage_one
            |
            |// START homepage_two
            |  Content from snippet **two**
            |    plus additional content
            |// END homepage_two
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
            |               "baseDirs": "src/test",
            |               "startPattern": "^.*?START(.+?)${'$'}",
            |               "endPattern": "^.*?END(.+?)${'$'}"
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
                        +"Content from snippet "
                        strong { +"one" }
                    }
                    br()
                    p {
                        +"Content from snippet "
                        strong { +"two" }
                        +"  plus additional content"
                    }
                }
            }
    }

    @Test
    @DisplayName("Test creating snippets from default `embedded` adapter config, but with tags and selecting from those tags")
    fun test23() {
        resource(
            "homepage.txt",
            """
            |---
            |---
            |{% snippets 'tag_a' %}
            |<br>
            |{% snippets 'tag_b' %}
            """.trimMargin()
        )
        resource(
            "src/test/kotlin/fileWithSnippets.md",
            """
            |// snippet::homepage_one[tag_a, tag_b, tag_c]
            |  Content from snippet **one**
            |// end::homepage_one
            |
            |// snippet::homepage_two[tag_a]
            |  Content from snippet **two**
            |    plus additional content
            |// end::homepage_two
            |
            |// snippet::homepage_three[tag_a]
            |  This malformed snippet will not be accepted as a valid snippet
            |// end::homepage_three[]
            |
            |// snippet::homepage_three[tag_a] This malformed  
            |  snippet will not be accepted as a valid snippet
            |// end::homepage_three[]
            |
            |// snippet::homepage_four[tag_a]
            |  This snippet which did not define an ending name will work find, though
            |// end::
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
            |               "baseDirs": "src/test"
            |           },
            |           "foo": "bar"
            |       }
            |   ]
            |}
            """.trimMargin()
        )
        enableLogging()

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p {
                        +"Content from snippet "
                        strong { +"one" }
                    }
                    p {
                        +"Content from snippet "
                        strong { +"two" }
                        +"  plus additional content"
                    }
                    p {
                        +"This snippet which did not define an ending name will work find, though"
                    }
                    br()
                    p {
                        +"Content from snippet "
                        strong { +"one" }
                    }
                }
            }
    }

// Remote Snippets Adapter
//----------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Test creating snippets from configured `remote` adapter config")
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
            |           "adapter": {
            |               "type": "remote",
            |               "url": "https://github.com/copper-leaf/wiki-with-sidebar/wiki/GettingStarted",
            |               "selector": "#wiki-body",
            |               "name": "one"
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
                    div("markdown-body") {
                        p { +"This is how to get started:" }
                        ol {
                            li {+"asdf"}
                            li {+"asdf"}
                            li {+"asdf"}
                        }
                    }
                }
            }
    }

}
