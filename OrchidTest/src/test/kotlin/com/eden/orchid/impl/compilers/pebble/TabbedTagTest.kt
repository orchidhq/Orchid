package com.eden.orchid.impl.compilers.pebble

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.registration.IgnoreModule
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.asHtml
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.innerHtmlMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.strikt.select
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import com.eden.orchid.utilities.addToSet
import kotlinx.html.li
import kotlinx.html.ul
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import strikt.api.expectThat

class TabbedTagTest : OrchidIntegrationTest(TabbedTagModule(), withGenerator<HomepageGenerator>()) {

    @BeforeEach
    internal fun setUp() {
        resource(
            "templates/tags/hellos.peb",
            """
            |<ul>
            |{% for tab in tag.tabs %}
            |    <li>
            |       {{ tab.content | raw }}
            |    </li>
            |{% endfor %}
            |</ul>
            """.trimMargin()
        )
    }

    @Test
    fun testNonDynamicTabs() {
        val input = """
            |{% hellos %}
            |
            |  {% hello1 %}
            |    Hello, world (1)
            |  {% endhello1 %}
            |
            |  {% hello2 %}
            |    Hello, world (2)
            |  {% endhello2 %}
            |
            |  {% hello3 %}
            |    Hello, world (3)
            |  {% endhello3 %}
            |
            |{% endhellos %}
        """.trimMargin()

        resource("homepage.peb", input.trim())

        expectThat(execute())
            .pageWasRendered("/index.html") {
                get { content }
                    .asHtml(true)
                    .select("body") {
                        innerHtmlMatches {
                            ul {
                                li { +"Hello, world (1)" }
                                li { +"Hello, world (2)" }
                                li { +"Hello, world (3)" }
                            }
                        }
                    }
            }
    }

    @Test
    fun testDynamicTabsWithForLoop() {
        val input = """
            |{% hellos dynamic %}
            |  {% for i in range(1, 3) %}
            |    {% hello "hello_tab_#{i}" %}
            |      Hello, world ({{ i }})
            |    {% endhello %}
            |  {% endfor %}
            |{% endhellos %}
        """.trimMargin()

        resource("homepage.peb", input.trim())

        expectThat(execute())
            .pageWasRendered("/index.html") {
                get { content }
                    .asHtml(true)
                    .select("body") {
                        innerHtmlMatches {
                            ul {
                                li { +"Hello, world (1)" }
                                li { +"Hello, world (2)" }
                                li { +"Hello, world (3)" }
                            }
                        }
                    }
            }
    }

    @Test
    fun testDynamicTabsWithForIf() {
        resource(
            "homepage.peb",
            """
            |{% hellos dynamic %}
            |  {% for i in fullRange %}
            |    {% if validValues contains i %}
            |      {% hello "hello_tab_#{i}" %}
            |        Hello, world ({{ i }})
            |      {% endhello %}
            |    {% endif %}
            |  {% endfor %}
            |{% endhellos %}
            """.trimMargin(),
            mapOf(
                "fullRange" to (0..10).toList(),
                "validValues" to listOf(3, 6, 7)
            )
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    ul {
                        li { +"Hello, world (3)" }
                        li { +"Hello, world (6)" }
                        li { +"Hello, world (7)" }
                    }
                }
            }
    }

    @Test
    fun testDynamicTabsWithNoLogic() {
        resource(
            "homepage.peb",
            """
            |{% hellos dynamic %}
            |   {% hello "hello_tab_2" %}
            |       Hello, world (2)
            |   {% endhello %}
            |   {% hello "hello_tab_3" %}
            |       Hello, world (3)
            |   {% endhello %}
            |   {% hello "hello_tab_4" %}
            |       Hello, world (4)
            |   {% endhello %}
            |{% endhellos %}
            """.trimMargin(),
            mapOf(
                "fullRange" to (0..10).toList(),
                "validValues" to listOf(3, 6, 7)
            )
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    ul {
                        li { +"Hello, world (2)" }
                        li { +"Hello, world (3)" }
                        li { +"Hello, world (4)" }
                    }
                }
            }
    }
}

class TabbedHellosTag : TemplateTag("hellos", Type.Tabbed, true) {

    override fun parameters() = emptyArray<String>()

    override fun getNewTab(key: String?, content: String?): TemplateTag.Tab {
        return Tab(key, content)
    }

    class Tab(key: String?, content: String?) : TemplateTag.SimpleTab("hello", key, content) {
        override fun parameters() = emptyArray<String>()
    }

}

@IgnoreModule
private class TabbedTagModule : OrchidModule() {
    override fun configure() {
        addToSet<TemplateTag, TabbedHellosTag>()
    }
}
