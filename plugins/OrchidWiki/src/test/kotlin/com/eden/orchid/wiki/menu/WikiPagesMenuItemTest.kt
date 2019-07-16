package com.eden.orchid.wiki.menu

import com.eden.orchid.strikt.asHtml
import com.eden.orchid.strikt.innerHtml
import com.eden.orchid.strikt.matches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.strikt.select
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.TestResults
import com.eden.orchid.wiki.WikiModule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@DisplayName("Tests page-rendering behavior of Wiki generator")
class WikiPagesMenuItemTest : OrchidIntegrationTest(WikiModule()) {

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

    private fun setupWikiSection(sectionBase: String?) {
        val basePath = if (sectionBase != null) "wiki/$sectionBase" else "wiki"

        resource("$basePath/summary.md", """
            * [1](page-one.md)
            * [2](page-two.md)
            * [3-4](page-three/page-four.md)
        """.trimIndent())
        resource("$basePath/page-one.md")
        resource("$basePath/page-two.md")
        resource("$basePath/page-three/page-four.md")
    }

    private fun TestResults.verifyWikiSection(sectionBase: String?) {
        val basePath = if (sectionBase != null) "wiki/$sectionBase" else "wiki"

        expectThat(this).pageWasRendered("/$basePath/index.html")
        expectThat(this).pageWasRendered("/$basePath/page-one/index.html")
        expectThat(this).pageWasRendered("/$basePath/page-two/index.html")
        expectThat(this).pageWasRendered("/$basePath/page-three/page-four/index.html")
    }

    @Test
    @DisplayName("Test WikiPagesMenuItem with only default section")
    fun test01() {
        configObject("theme", """{"menu": [{"type": "wiki"}]}""")

        setupWikiSection(null)
        val testResults = execute()
        testResults.verifyWikiSection(null)

        expectThat(testResults)
                .pageWasRendered("/wiki/index.html")
                .get { content }
                .asHtml(removeComments = true)
                .select("body #menu")
                .matches()
                .innerHtml()
                .isEqualTo("""
                    <li>
                      <a href="http://orchid.test/wiki/page-one">1</a>
                    </li>
                    <li>
                      <a href="http://orchid.test/wiki/page-two">2</a>
                    </li>
                    <li>
                      <span class="submenu">Page-three</span>
                      <ul>
                        <li>
                          <a href="http://orchid.test/wiki/page-three/page-four">3-4</a>
                        </li>
                      </ul>
                    </li>
                """.trimIndent())
    }

    @Test
    @DisplayName("Test WikiPagesMenuItem with only default section, and specifying a menu item section")
    fun test02() {
        configObject("theme", """{"menu": [{"type": "wiki", "section": "wiki"}]}""")

        setupWikiSection(null)
        val testResults = execute()
        testResults.verifyWikiSection(null)

        expectThat(testResults)
                .pageWasRendered("/wiki/index.html")
                .get { content }
                .asHtml(removeComments = true)
                .select("body #menu")
                .matches()
                .innerHtml()
                .isEqualTo("""
                    <li>
                      <a href="http://orchid.test/wiki/page-one">1</a>
                    </li>
                    <li>
                      <a href="http://orchid.test/wiki/page-two">2</a>
                    </li>
                    <li>
                      <span class="submenu">Page-three</span>
                      <ul>
                        <li>
                          <a href="http://orchid.test/wiki/page-three/page-four">3-4</a>
                        </li>
                      </ul>
                    </li>
                """.trimIndent())
    }

    @Test
    @DisplayName("Test WikiPagesMenuItem with only multiple sections, and not setting a section on the menu item")
    fun test03() {
        configObject("theme", """{"menu": [{"type": "wiki"}]}""")
        configObject("wiki", """{"sections": ["section1", "section2"]}""")

        setupWikiSection("section1")
        setupWikiSection("section2")
        val testResults = execute()
        testResults.verifyWikiSection("section1")
        testResults.verifyWikiSection("section2")

        expectThat(testResults)
                .pageWasRendered("/wiki/index.html")
                .get { content }
                .asHtml(removeComments = true)
                .select("body #menu")
                .matches()
                .innerHtml()
                .isEqualTo("""
                    <li>
                      <span class="submenu">Section1</span>
                      <ul>
                        <li>
                          <a href="http://orchid.test/wiki/section1/page-one">1</a>
                        </li>
                        <li>
                          <a href="http://orchid.test/wiki/section1/page-two">2</a>
                        </li>
                        <li>
                          <span class="submenu">Page-three</span>
                          <ul>
                            <li>
                              <a href="http://orchid.test/wiki/section1/page-three/page-four">3-4</a>
                            </li>
                          </ul>
                        </li>
                      </ul>
                    </li>
                    <li>
                      <span class="submenu">Section2</span>
                      <ul>
                        <li>
                          <a href="http://orchid.test/wiki/section2/page-one">1</a>
                        </li>
                        <li>
                          <a href="http://orchid.test/wiki/section2/page-two">2</a>
                        </li>
                        <li>
                          <span class="submenu">Page-three</span>
                          <ul>
                            <li>
                              <a href="http://orchid.test/wiki/section2/page-three/page-four">3-4</a>
                            </li>
                          </ul>
                        </li>
                      </ul>
                    </li>
                """.trimIndent())
    }

    @Test
    @DisplayName("Test WikiPagesMenuItem with only multiple sections, and setting a section on the menu item")
    fun test04() {
        configObject("theme", """{"menu": [{"type": "wiki", "section": "section1"}]}""")
        configObject("wiki", """{"sections": ["section1", "section2"]}""")

        setupWikiSection("section1")
        setupWikiSection("section2")
        val testResults = execute()
        testResults.verifyWikiSection("section1")
        testResults.verifyWikiSection("section2")

        expectThat(testResults)
                .pageWasRendered("/wiki/index.html")
                .get { content }
                .asHtml(removeComments = true)
                .select("body #menu")
                .matches()
                .innerHtml()
                .isEqualTo("""
                    <li>
                      <a href="http://orchid.test/wiki/section1/page-one">1</a>
                    </li>
                    <li>
                      <a href="http://orchid.test/wiki/section1/page-two">2</a>
                    </li>
                    <li>
                      <span class="submenu">Page-three</span>
                      <ul>
                        <li>
                          <a href="http://orchid.test/wiki/section1/page-three/page-four">3-4</a>
                        </li>
                      </ul>
                    </li>
                """.trimIndent())
    }

    @Test
    @DisplayName("Test WikiPagesMenuItem with only multiple sections, and setting a non-existant section on the menu item")
    fun test05() {
        configObject("theme", """{"menu": [{"type": "wiki", "section": "section3"}]}""")
        configObject("wiki", """{"sections": ["section1", "section2"]}""")

        setupWikiSection("section1")
        setupWikiSection("section2")
        val testResults = execute()
        testResults.verifyWikiSection("section1")
        testResults.verifyWikiSection("section2")

        expectThat(testResults)
                .pageWasRendered("/wiki/index.html")
                .get { content }
                .asHtml(removeComments = true)
                .select("body #menu")
                .matches()
                .innerHtml()
                .isEqualTo("")
    }

}
