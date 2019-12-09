package com.eden.orchid.pages.menu

import com.eden.orchid.pages.PagesModule
import com.eden.orchid.strikt.asHtml
import com.eden.orchid.strikt.outerHtmlMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.strikt.select
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import kotlinx.html.a
import kotlinx.html.id
import kotlinx.html.li
import kotlinx.html.span
import kotlinx.html.ul
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

@DisplayName("Tests menu items from Pages plugin")
class PageIdsMenuItemTest : OrchidIntegrationTest(PagesModule()) {

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

    @Test
    @DisplayName("Test PageIds menu item with flat structure")
    fun test01() {
        configObject("theme", """{"menu": [{"type": "pageIds"}]}""")

        resource(
            "pages/page-one.md", """
            | # Header 1
            |
            | ## Header 1-1
            |
            | ## Header 1-2
            |
            | # Header 2
            |
            | ## Header 2-1
            |
            | ### Header 2-1-1
            |
            | ## Header 2-2
            |
            | ### Header 2-2-1
            |
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/page-one/index.html") {
                get { content }
                    .asHtml()
                    .select("body #menu") {
                        outerHtmlMatches {
                            ul {
                                id = "menu"

                                li { a(href = "#header-1") { +"Header 1" } }
                                li { a(href = "#header-1-1") { +"Header 1-1" } }
                                li { a(href = "#header-1-2") { +"Header 1-2" } }

                                li { a(href = "#header-2") { +"Header 2" } }
                                li { a(href = "#header-2-1") { +"Header 2-1" } }
                                li { a(href = "#header-2-1-1") { +"Header 2-1-1" } }
                                li { a(href = "#header-2-2") { +"Header 2-2" } }
                                li { a(href = "#header-2-2-1") { +"Header 2-2-1" } }
                            }
                        }
                    }
            }
    }

    @Test
    @DisplayName("Test PageIds menu item with nested structure")
    fun test02() {
        configObject("theme", """{"menu": [{"type": "pageIds", "structure": "nested"}]}""")

        resource(
            "pages/page-one.md", """
            | # Header 1
            |
            | ## Header 1-1
            |
            | ## Header 1-2
            |
            | # Header 2
            |
            | ## Header 2-1
            |
            | ### Header 2-1-1
            |
            | ## Header 2-2
            |
            | ### Header 2-2-1
            |
        """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/page-one/index.html") {
                get { content }
                    .asHtml()
                    .select("body #menu") {
                        outerHtmlMatches {
                            ul {
                                id = "menu"
                                li {
                                    span("submenu") { +"Header 1" }
                                    ul {
                                        li { a(href = "#header-1-1") { +"Header 1-1" } }
                                        li { a(href = "#header-1-2") { +"Header 1-2" } }
                                    }
                                }

                                li {
                                    span("submenu") { +"Header 2" }
                                    ul {
                                        li {
                                            span("submenu") { +"Header 2-1" }
                                            ul {
                                                li { a(href = "#header-2-1-1") { +"Header 2-1-1" } }
                                            }
                                        }
                                        li {
                                            span("submenu") { +"Header 2-2" }
                                            ul {
                                                li { a(href = "#header-2-2-1") { +"Header 2-2-1" } }
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
    @DisplayName("Test PageIds menu item with nested structure and max level")
    fun test03() {
        configObject(
            "theme",
            """{"menu": [{"type": "pageIds", "structure": "nested", "maxLevel": 1, "minLevel": 2}]}"""
        )

        resource(
            "pages/page-one.md", """
            | # Header 1
            |
            | ## Header 1-1
            |
            | ## Header 1-2
            |
            | # Header 2
            |
            | ## Header 2-1
            |
            | ### Header 2-1-1
            |
            | ## Header 2-2
            |
            | ### Header 2-2-1
            |
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/page-one/index.html") {
                get { content }
                    .asHtml()
                    .select("body #menu") {
                        outerHtmlMatches {
                            ul {
                                id = "menu"
                                li {
                                    span("submenu") { +"Header 1" }
                                    ul {
                                        li { a(href = "#header-1-1") { +"Header 1-1" } }
                                        li { a(href = "#header-1-2") { +"Header 1-2" } }
                                    }
                                }

                                li {
                                    span("submenu") { +"Header 2" }
                                    ul {
                                        li { a(href = "#header-2-1") { +"Header 2-1" } }
                                        li { a(href = "#header-2-2") { +"Header 2-2" } }
                                    }
                                }
                            }
                        }
                    }
            }
    }

    @Test
    @DisplayName("Test PageIds menu item with nested structure and min level")
    fun test04() {
        configObject(
            "theme",
            """{"menu": [{"type": "pageIds", "structure": "nested", "maxLevel": 2, "minLevel": 3}]}"""
        )

        resource(
            "pages/page-one.md", """
            | # Header 1
            |
            | ## Header 1-1
            |
            | ## Header 1-2
            |
            | # Header 2
            |
            | ## Header 2-1
            |
            | ### Header 2-1-1
            |
            | ## Header 2-2
            |
            | ### Header 2-2-1
            |
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/page-one/index.html") {
                get { content }
                    .asHtml()
                    .select("body #menu") {
                        outerHtmlMatches {
                            ul {
                                id = "menu"
                                li { a(href = "#header-1-1") { +"Header 1-1" } }
                                li { a(href = "#header-1-2") { +"Header 1-2" } }
                                li {
                                    span("submenu") { +"Header 2-1" }
                                    ul {
                                        li { a(href = "#header-2-1-1") { +"Header 2-1-1" } }
                                    }
                                }
                                li {
                                    span("submenu") { +"Header 2-2" }
                                    ul {
                                        li { a(href = "#header-2-2-1") { +"Header 2-2-1" } }
                                    }
                                }
                            }
                        }
                    }
            }
    }

}
