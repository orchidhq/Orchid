package com.eden.orchid.wiki.menu

import com.eden.orchid.strikt.asHtml
import com.eden.orchid.strikt.outerHtmlMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.strikt.select
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.TestResults
import com.eden.orchid.wiki.WikiModule
import kotlinx.html.a
import kotlinx.html.id
import kotlinx.html.li
import kotlinx.html.span
import kotlinx.html.ul
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.Assertion
import strikt.api.expectThat

@DisplayName("Tests page-rendering behavior of Wiki generator")
class WikiPagesMenuItemTest : OrchidIntegrationTest(WikiModule()) {

    @BeforeEach
    fun setUp() {
        resource(
            "templates/layouts/index.peb", """
            <!DOCTYPE HTML>
            <html>
            <head>
            {% head %}
            {% styles %}
            </head>
            <body>
            {% page %}
            <ul id="menu">
            {% for menuItem in theme.menu.getMenuItems(page) %}
                {% include 'includes/menuItem' with {"menuItem": menuItem} %}
            {% endfor %}
            </ul>
            {% scripts %}
            </body>
            </html>
        """.trimIndent()
        )

        resource(
            "templates/includes/menuItem.peb", """
            {% if menuItem.hasChildren %}
                <li>
                    <span class="submenu">{{ menuItem.title | title }}</span>
                    <ul>
                    {% for childLink in menuItem.children %}
                        {% include 'includes/menuItem' with {"menuItem": childLink} %}
                    {% endfor %}
                    </ul>
                </li>
            {% elseif menuItem.isSeparator() %}
                {% if menuItem.title|length > 0 %}
                    <li><a class="subheader">{{ menuItem.title }}</a></li>
                {% else %}
                    <li><div class="divider"></div></li>
                {% endif %}
            {% else %}
                {% if menuItem.title|length > 0 %}
                <li><a href="{{ menuItem.link }}">{{ menuItem.title }}</a></li>
                {% endif %}
            {% endif %}
            """.trimIndent()
        )
    }

    private fun setupWikiSection(sectionBase: String?) {
        val basePath = if (sectionBase != null) "wiki/$sectionBase" else "wiki"

        resource(
            "$basePath/summary.md", """
            * [1](page-one.md)
            * [2](page-two.md)
            * [3-4](page-three/page-four.md)
        """.trimIndent()
        )
        resource("$basePath/page-one.md")
        resource("$basePath/page-two.md")
        resource("$basePath/page-three/page-four.md")
    }

    private fun Assertion.Builder<TestResults>.verifyWikiSection(sectionBase: String?): Assertion.Builder<TestResults> {
        val basePath = if (sectionBase != null) "wiki/$sectionBase" else "wiki"

        pageWasRendered("/$basePath/index.html")
        pageWasRendered("/$basePath/page-one/index.html")
        pageWasRendered("/$basePath/page-two/index.html")
        pageWasRendered("/$basePath/page-three/page-four/index.html")

        return this
    }

    @Test
    @DisplayName("Test WikiPagesMenuItem with only default section")
    fun test01() {
        configObject("theme", """{"menu": [{"type": "wiki"}]}""")

        setupWikiSection(null)

        expectThat(execute())
            .verifyWikiSection(null)
            .pageWasRendered("/wiki/index.html") {
                get { content }
                    .asHtml()
                    .select("body #menu") {
                        outerHtmlMatches {
                            ul {
                                id = "menu"
                                li { a(href = "http://orchid.test/wiki/page-one") { +"1" } }
                                li { a(href = "http://orchid.test/wiki/page-two") { +"2" } }
                                li {
                                    span("submenu") { +"Page-three" }
                                    ul {
                                        li { a(href = "http://orchid.test/wiki/page-three/page-four") { +"3-4" } }
                                    }
                                }
                            }
                        }
                    }
            }
    }

    @Test
    @DisplayName("Test WikiPagesMenuItem with only default section, and specifying a menu item section")
    fun test02() {
        configObject("theme", """{"menu": [{"type": "wiki", "section": "wiki"}]}""")

        setupWikiSection(null)

        expectThat(execute())
            .verifyWikiSection(null)
            .pageWasRendered("/wiki/index.html") {
                get { content }
                    .asHtml()
                    .select("body #menu") {
                        outerHtmlMatches {
                            ul {
                                id = "menu"
                                li { a(href = "http://orchid.test/wiki/page-one") { +"1" } }
                                li { a(href = "http://orchid.test/wiki/page-two") { +"2" } }
                                li {
                                    span("submenu") { +"Page-three" }
                                    ul {
                                        li { a(href = "http://orchid.test/wiki/page-three/page-four") { +"3-4" } }
                                    }
                                }
                            }
                        }
                    }
            }
    }

    @Test
    @DisplayName("Test WikiPagesMenuItem with only multiple sections, and not setting a section on the menu item")
    fun test03() {
        configObject("theme", """{"menu": [{"type": "wiki"}]}""")
        configObject("wiki", """{"sections": ["section1", "section2"]}""")

        setupWikiSection("section1")
        setupWikiSection("section2")

        expectThat(execute())
            .verifyWikiSection("section1")
            .verifyWikiSection("section2")
            .pageWasRendered("/wiki/index.html") {
                get { content }
                    .asHtml()
                    .select("body #menu") {
                        outerHtmlMatches {
                            ul {
                                id = "menu"
                                li {
                                    span("submenu") { +"Section1" }
                                    ul {
                                        li { a(href = "http://orchid.test/wiki/section1/page-one") { +"1" } }
                                        li { a(href = "http://orchid.test/wiki/section1/page-two") { +"2" } }
                                        li {
                                            span("submenu") { +"Page-three" }
                                            ul {
                                                li { a(href = "http://orchid.test/wiki/section1/page-three/page-four") { +"3-4" } }
                                            }
                                        }
                                    }
                                }

                                li {
                                    span("submenu") { +"Section2" }
                                    ul {
                                        li { a(href = "http://orchid.test/wiki/section2/page-one") { +"1" } }
                                        li { a(href = "http://orchid.test/wiki/section2/page-two") { +"2" } }
                                        li {
                                            span("submenu") { +"Page-three" }
                                            ul {
                                                li { a(href = "http://orchid.test/wiki/section2/page-three/page-four") { +"3-4" } }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
            }
    }

    @Test
    @DisplayName("Test WikiPagesMenuItem with only multiple sections, and setting a section on the menu item")
    fun test04() {
        configObject("theme", """{"menu": [{"type": "wiki", "section": "section1"}]}""")
        configObject("wiki", """{"sections": ["section1", "section2"]}""")

        setupWikiSection("section1")
        setupWikiSection("section2")

        expectThat(execute())
            .verifyWikiSection("section1")
            .verifyWikiSection("section2")
            .pageWasRendered("/wiki/index.html") {
                get { content }
                    .asHtml()
                    .select("body #menu") {
                        outerHtmlMatches {
                            ul {
                                id = "menu"
                                li { a(href = "http://orchid.test/wiki/section1/page-one") { +"1" } }
                                li { a(href = "http://orchid.test/wiki/section1/page-two") { +"2" } }
                                li {
                                    span("submenu") { +"Page-three" }
                                    ul {
                                        li { a(href = "http://orchid.test/wiki/section1/page-three/page-four") { +"3-4" } }
                                    }
                                }
                            }
                        }
                    }
            }
    }

    @Test
    @DisplayName("Test WikiPagesMenuItem with only multiple sections, and setting a non-existant section on the menu item")
    fun test05() {
        configObject("theme", """{"menu": [{"type": "wiki", "section": "section3"}]}""")
        configObject("wiki", """{"sections": ["section1", "section2"]}""")

        setupWikiSection("section1")
        setupWikiSection("section2")

        expectThat(execute())
            .verifyWikiSection("section1")
            .verifyWikiSection("section2")
            .pageWasRendered("/wiki/index.html") {
                get { content }
                    .asHtml()
                    .select("body #menu") {
                        outerHtmlMatches {
                            ul {
                                id = "menu"
                            }
                        }
                    }
            }
    }

}
