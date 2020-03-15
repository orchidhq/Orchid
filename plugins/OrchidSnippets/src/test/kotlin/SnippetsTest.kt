package com.eden.orchid.snippets

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.plugindocs.PluginDocsModule
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import kotlinx.html.a
import kotlinx.html.br
import kotlinx.html.div
import kotlinx.html.em
import kotlinx.html.hr
import kotlinx.html.id
import kotlinx.html.li
import kotlinx.html.ol
import kotlinx.html.p
import kotlinx.html.role
import kotlinx.html.strong
import kotlinx.html.ul
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

class SnippetsTest : OrchidIntegrationTest(SnippetsModule(), PluginDocsModule(), withGenerator<HomepageGenerator>()) {

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
        flag("diagnose", true)
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
                    div {
                        id = "tag_one"
                        ul("nav nav-tabs") {
                            role = "tablist"

                            li("active") {
                                role = "presentation"
                                a(href = "#tag_one-one") {
                                    role = "tab"
                                    attributes["aria-controls"] = "home"
                                    attributes["data-toggle"] = "tab"
                                    +"one"
                                }
                            }
                        }
                    }
                    div("tab-content") {
                        div("tab-pane fade in active") {
                            id = "tag_one-one"
                            role = "tabpanel"
                            p {
                                +"Content from snippet "
                                strong { +"one" }
                            }
                        }
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
                        div {
                            id = "tag_one"
                            ul("nav nav-tabs") {
                                role = "tablist"

                                li("active") {
                                    role = "presentation"
                                    a(href = "#tag_one-one") {
                                        role = "tab"
                                        attributes["aria-controls"] = "home"
                                        attributes["data-toggle"] = "tab"
                                        +"one"
                                    }
                                }
                            }
                        }
                        div("tab-content") {
                            div("tab-pane fade in active") {
                                id = "tag_one-one"
                                role = "tabpanel"
                                p {
                                    +"Content from snippet "
                                    strong { +"one" }
                                }
                            }
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
            |           "adapter": "file"
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
            |           }
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
            |           }
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
            |           }
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
        serveOn(8080)
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
            |// snippet::homepage_three
            |  This malformed snippet will not be accepted as a valid snippet because it is missing the trailing [] on
            |    start delimiter
            |// end::homepage_three[]
            |
            |// snippet::homepage_three[tag_a]
            |  This malformed snippet will not be accepted as a valid snippet because of the trailing [] on end 
            |    delimiter
            |// end::homepage_three[]
            |
            |// snippet::homepage_three[tag_a] This malformed  
            |  snippet will not be accepted as a valid snippet because the start tag does not end the line
            |// end::homepage_three[]
            |
            |// snippet::homepage_four[tag_a]
            |  This snippet which did not define an ending name will work fine, though
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
            |           }
            |       }
            |   ]
            |}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    div {
                        id = "tag_a"
                        ul("nav nav-tabs") {
                            role = "tablist"

                            li("active") {
                                role = "presentation"
                                a(href = "#tag_a-homepage_four") {
                                    role = "tab"
                                    attributes["aria-controls"] = "home"
                                    attributes["data-toggle"] = "tab"
                                    +"homepage_four"
                                }
                            }
                            li("") {
                                role = "presentation"
                                a(href = "#tag_a-homepage_one") {
                                    role = "tab"
                                    attributes["aria-controls"] = "home"
                                    attributes["data-toggle"] = "tab"
                                    +"homepage_one"
                                }
                            }
                            li("") {
                                role = "presentation"
                                a(href = "#tag_a-homepage_two") {
                                    role = "tab"
                                    attributes["aria-controls"] = "home"
                                    attributes["data-toggle"] = "tab"
                                    +"homepage_two"
                                }
                            }
                        }
                    }
                    div("tab-content") {
                        div("tab-pane fade in active") {
                            id = "tag_a-homepage_four"
                            role = "tabpanel"
                            p {
                                +"This snippet which did not define an ending name will work fine, though"
                            }
                        }
                        div("tab-pane fade") {
                            id = "tag_a-homepage_one"
                            role = "tabpanel"
                            p {
                                +"Content from snippet "
                                strong { +"one" }
                            }
                        }
                        div("tab-pane fade") {
                            id = "tag_a-homepage_two"
                            role = "tabpanel"
                            p {
                                +"Content from snippet "
                                strong { +"two" }
                                +"  plus additional content"
                            }
                        }
                    }

                    br()

                    div {
                        id = "tag_b"
                        ul("nav nav-tabs") {
                            role = "tablist"

                            li("active") {
                                role = "presentation"
                                a(href = "#tag_b-homepage_one") {
                                    role = "tab"
                                    attributes["aria-controls"] = "home"
                                    attributes["data-toggle"] = "tab"
                                    +"homepage_one"
                                }
                            }
                        }
                    }
                    div("tab-content") {
                        div("tab-pane fade in active") {
                            id = "tag_b-homepage_one"
                            role = "tabpanel"
                            p {
                                +"Content from snippet "
                                strong { +"one" }
                            }
                        }
                    }
                }
            }
    }

// Remote Snippets Adapter
//----------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Test creating snippets from configured `remote` adapter config, with single selector")
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
            |           }
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
                            li { +"asdf" }
                            li { +"asdf" }
                            li { +"asdf" }
                        }
                    }
                }
            }
    }

    @Test
    @DisplayName("Test creating snippets from configured `remote` adapter config, with single selector")
    fun test32() {
        resource(
            "homepage.txt",
            """
            |---
            |---
            |{% set bodySnippet = snippet('body') %}
            |{% set sidebarSnippet = snippet('sidebar') %}
            |
            |{{ bodySnippet.content | raw }}
            |<br>
            |{{ bodySnippet.tags|join(',') }}
            |
            |<hr>
            |
            |{{ sidebarSnippet.content | raw }}
            |<br>
            |{{ sidebarSnippet.tags|join(',') }}
            """.trimMargin()
        )
        configObject(
            "snippets",
            """
            |{
            |   "sections": [
            |       {
            |           "tags": ["wiki"],
            |           "adapter": {
            |               "type": "remote",
            |               "url": "https://github.com/copper-leaf/wiki-with-sidebar/wiki/GettingStarted",
            |               "selectors": [
            |                   {
            |                       "selector": "#wiki-body",
            |                       "name": "body",
            |                       "tags": ["body"]
            |                   },
            |                   {
            |                       "selector": "#wiki-rightbar .wiki-custom-sidebar.markdown-body",
            |                       "name": "sidebar",
            |                       "tags": ["sidebar"]
            |                   }
            |               ]
            |           }
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
                            li { +"asdf" }
                            li { +"asdf" }
                            li { +"asdf" }
                        }
                    }
                    br()
                    +"body,wiki"

                    hr()

                    ul {
                        li { a(href="Home") { +"Home" } }
                        li {
                            a(href="GettingStarted") { +"Getting Started" }
                            ul {
                                li { a(href="Installation") { +"Installation" } }
                                li { a(href="Configuration") { +"Configuration" } }
                            }
                        }
                    }
                    br()
                    +"sidebar,wiki"
                }
            }
    }

}
