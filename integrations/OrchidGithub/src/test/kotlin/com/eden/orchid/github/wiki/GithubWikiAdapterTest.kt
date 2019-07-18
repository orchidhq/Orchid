package com.eden.orchid.github.wiki

import com.eden.orchid.github.GithubModule
import com.eden.orchid.strikt.asHtml
import com.eden.orchid.strikt.nothingElseRendered
import com.eden.orchid.strikt.outerHtmlMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.strikt.pagesGenerated
import com.eden.orchid.strikt.select
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.wiki.WikiModule
import kotlinx.html.a
import kotlinx.html.li
import kotlinx.html.ul
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

@DisplayName("Tests page-rendering behavior of Wiki generator")
class GithubWikiAdapterTest : OrchidIntegrationTest(WikiModule(), GithubModule()) {

    @Test
    @DisplayName("Wikis can be imported from Github. If no _Sidebar file is present, files will be listed alphabetically.")
    fun test01() {
        configObject(
            "wiki", """
            {
                "sections": {
                    "wiki-without-sidebar": {
                        "adapter": {
                            "type": "github",
                            "repo": "copper-leaf/wiki-without-sidebar"
                        }
                    }
                }
            }
            """.trimIndent()
        )

        expectThat(execute())
            .pagesGenerated(6)
            .pageWasRendered("/wiki/wiki-without-sidebar/index.html") {
                get { content }
                    .asHtml()
                    .select("body > ul") {
                        outerHtmlMatches {
                            ul {
                                li { a(href = "http://orchid.test/wiki/wiki-without-sidebar/Configuration") { +"Configuration" } }
                                li { a(href = "http://orchid.test/wiki/wiki-without-sidebar/GettingStarted") { +"Getting Started" } }
                                li { a(href = "http://orchid.test/wiki/wiki-without-sidebar/Home") { +"Home" } }
                                li { a(href = "http://orchid.test/wiki/wiki-without-sidebar/Installation") { +"Installation" } }
                            }
                        }
                    }
            }
            .pageWasRendered("/wiki/wiki-without-sidebar/Configuration/index.html")
            .pageWasRendered("/wiki/wiki-without-sidebar/GettingStarted/index.html")
            .pageWasRendered("/wiki/wiki-without-sidebar/Home/index.html")
            .pageWasRendered("/wiki/wiki-without-sidebar/Installation/index.html")
            .pageWasRendered("/favicon.ico")
            .nothingElseRendered()
    }

    @Test
    @DisplayName("Wikis can be imported from Github. If a _Sidebar file is present, it will be used as the Summary page.")
    fun test02() {
        configObject(
            "wiki", """
            {
                "sections": {
                    "wiki-with-sidebar": {
                        "adapter": {
                            "type": "github",
                            "repo": "copper-leaf/wiki-with-sidebar"
                        }
                    }
                }
            }
            """.trimIndent()
        )

        expectThat(execute())
            .pagesGenerated(6)
            .pageWasRendered("/wiki/wiki-with-sidebar/index.html") {
                get { content }
                    .asHtml()
                    .select("body > ul") {
                        outerHtmlMatches {
                            ul {
                                li { a(href = "http://orchid.test/wiki/wiki-with-sidebar/Home") { +"Home" } }
                                li {
                                    a(href = "http://orchid.test/wiki/wiki-with-sidebar/GettingStarted") { +"Getting Started" }
                                    ul {
                                        li { a(href = "http://orchid.test/wiki/wiki-with-sidebar/Installation") { +"Installation" } }
                                        li { a(href = "http://orchid.test/wiki/wiki-with-sidebar/Configuration") { +"Configuration" } }
                                    }
                                }
                            }
                        }
                    }
            }
            .pageWasRendered("/wiki/wiki-with-sidebar/Configuration/index.html")
            .pageWasRendered("/wiki/wiki-with-sidebar/GettingStarted/index.html")
            .pageWasRendered("/wiki/wiki-with-sidebar/Home/index.html")
            .pageWasRendered("/wiki/wiki-with-sidebar/Installation/index.html")
            .pageWasRendered("/favicon.ico")
            .nothingElseRendered()
    }

}
