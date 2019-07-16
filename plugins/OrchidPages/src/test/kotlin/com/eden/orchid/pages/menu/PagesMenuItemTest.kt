package com.eden.orchid.pages.menu

import com.eden.orchid.pages.PagesModule
import com.eden.orchid.strikt.asHtml
import com.eden.orchid.strikt.innerHtml
import com.eden.orchid.strikt.matches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.strikt.select
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@DisplayName("Tests menu items from Pages plugin")
class PagesMenuItemTest : OrchidIntegrationTest(PagesModule()) {

    @BeforeEach
    fun setUp() {
        resource("templates/layouts/index.peb", """
            <!DOCTYPE HTML>
            <html>
            <head>
            {% head %}
            {% styles %}
            </head>
            <body>
            {% page %}
            <div id="menu">
            {% for menuItem in theme.menu.getMenuItems(page) %}
                {% include 'includes/menuItem' with {"menuItem": menuItem} %}
            {% endfor %}
            </div>
            {% scripts %}
            </body>
            </html>
        """.trimIndent())

        resource("templates/includes/menuItem.peb", """
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
        """.trimIndent())
    }

    @Test
    @DisplayName("Test static pages menu item")
    fun test01() {
        configObject("theme", """{"menu": [{"type": "pages"}]}""")

        resource("pages/page-1.md")
        resource("pages/page-2.md")
        resource("pages/page-3.md")

        val testResults = execute()

        expectThat(testResults)
                .pageWasRendered("/page-1/index.html")
                .get { content }
                .asHtml(removeComments = true)
                .select("body #menu")
                .matches()
                .innerHtml()
                .isEqualTo("""
                    <li>
                      <a href="http://orchid.test/page-1">Page 1</a>
                    </li>
                    <li>
                      <a href="http://orchid.test/page-2">Page 2</a>
                    </li>
                    <li>
                      <a href="http://orchid.test/page-3">Page 3</a>
                    </li>
                """.trimIndent())
    }

    @Test
    @DisplayName("Test static pages menu item with group")
    fun test02() {
        configObject("theme", """{"menu": [{"type": "pages", "group": "one"}]}""")

        resource("pages/one/page-1.md")
        resource("pages/one/page-2.md")
        resource("pages/one/page-3.md")

        resource("pages/two/page-1.md")
        resource("pages/two/page-2.md")
        resource("pages/two/page-3.md")

        val testResults = execute()

        expectThat(testResults)
                .pageWasRendered("/one/page-1/index.html")
                .get { content }
                .asHtml(removeComments = true)
                .select("body #menu")
                .matches()
                .innerHtml()
                .isEqualTo("""
                    <li>
                      <a href="http://orchid.test/one/page-1">Page 1</a>
                    </li>
                    <li>
                      <a href="http://orchid.test/one/page-2">Page 2</a>
                    </li>
                    <li>
                      <a href="http://orchid.test/one/page-3">Page 3</a>
                    </li>
                """.trimIndent())
    }

}
