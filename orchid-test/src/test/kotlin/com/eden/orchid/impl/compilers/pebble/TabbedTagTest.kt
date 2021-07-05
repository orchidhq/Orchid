package com.eden.orchid.impl.compilers.pebble

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.registration.IgnoreModule
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import com.eden.orchid.utilities.addToSet
import kotlinx.html.li
import kotlinx.html.ul
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import strikt.api.expectThat

class TabbedTagTest : OrchidIntegrationTest(TabbedTagModule(), withGenerator<HomepageGenerator>()) {

    @BeforeEach
    internal fun setUp() {
        resource(
            "templates/tags/hellos.peb",
            """
            |{{ tag.greeting }} {{ tag.closing }}
            |<ul>
            |{% for tab in tag.tabs %}
            |    <li>
            |       {{ tab.tabGreeting }} {{ tab.content | raw }} {{ tab.tabClosing }}
            |    </li>
            |{% endfor %}
            |</ul>
            """.trimMargin()
        )
        resource(
            "templates/tags/althellos.peb",
            """
            |alt {{ tag.greeting }} {{ tag.closing }}
            |<ul>
            |{% for tab in tag.tabs %}
            |    <li>
            |       {{ tab.tabGreeting }} {{ tab.content | raw }} {{ tab.tabClosing }}
            |    </li>
            |{% endfor %}
            |</ul>
            """.trimMargin()
        )
    }

    @Test
    fun testNonDynamicTabsNoFilter() {
        val input = """
            |{% hellos 'welcome' %}
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
                htmlBodyMatches {
                    +"welcome yall"
                    ul {
                        li { +"yo, Hello, world (1)" }
                        li { +"yo, Hello, world (2)" }
                        li { +"yo, Hello, world (3)" }
                    }
                }
            }
    }

    @Test
    fun testNonDynamicTabsParentFilter() {
        val input = """
            |{% hellos 'welcome' 'partner' :: upper %}
            |
            |  {% hello1 'hey,' ', wassup' %}
            |    Hello, world (1)
            |  {% endhello1 %}
            |
            |  {% hello2 'hi,' tabClosing=', wassup' %}
            |    Hello, world (2)
            |  {% endhello2 %}
            |
            |  {% hello3 tabGreeting='sup,' tabClosing=', wassup' %}
            |    Hello, world (3)
            |  {% endhello3 %}
            |
            |{% endhellos %}
        """.trimMargin()

        resource("homepage.peb", input.trim())

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    +"welcome partner"
                    ul {
                        li { +"hey, HELLO, WORLD (1) , wassup" }
                        li { +"hi, HELLO, WORLD (2) , wassup" }
                        li { +"sup, HELLO, WORLD (3) , wassup" }
                    }
                }
            }
    }

    @Test
    fun testNonDynamicTabsChildFilters() {
        val input = """
            |{% hellos 'welcome' closing='partner' %}
            |
            |  {% hello1 :: upper %}
            |    Hello, world (1)
            |  {% endhello1 %}
            |
            |  {% hello2 %}
            |    Hello, world (2)
            |  {% endhello2 %}
            |
            |  {% hello3 :: upper %}
            |    Hello, world (3)
            |  {% endhello3 %}
            |
            |{% endhellos %}
        """.trimMargin()

        resource("homepage.peb", input.trim())

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    +"welcome partner"
                    ul {
                        li { +"yo, HELLO, WORLD (1)" }
                        li { +"yo, Hello, world (2)" }
                        li { +"yo, HELLO, WORLD (3)" }
                    }
                }
            }
    }

    @Test
    fun testNonDynamicTabsAlternateTemplate() {
        val input = """
            |{% hellos 'welcome' closing='partner' template='althellos' %}
            |
            |  {% hello1 :: upper %}
            |    Hello, world (1)
            |  {% endhello1 %}
            |
            |  {% hello2 %}
            |    Hello, world (2)
            |  {% endhello2 %}
            |
            |  {% hello3 :: upper %}
            |    Hello, world (3)
            |  {% endhello3 %}
            |
            |{% endhellos %}
        """.trimMargin()

        resource("homepage.peb", input.trim())

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    +"alt welcome partner"
                    ul {
                        li { +"yo, HELLO, WORLD (1)" }
                        li { +"yo, Hello, world (2)" }
                        li { +"yo, HELLO, WORLD (3)" }
                    }
                }
            }
    }

    @Test
    fun testDynamicTabsWithForLoopNoFilter() {
        val input = """
            |{% hellos dynamic 'welcome' 'partner' %}
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
                htmlBodyMatches {
                    +"welcome partner"
                    ul {
                        li { +"yo, Hello, world (1)" }
                        li { +"yo, Hello, world (2)" }
                        li { +"yo, Hello, world (3)" }
                    }
                }
            }
    }

    @Test
    fun testDynamicTabsWithForLoopChildFilter() {
        val input = """
            |{% hellos dynamic 'welcome' %}
            |  {% for i in range(1, 3) %}
            |    {% hello "hello_tab_#{i}" :: upper %}
            |      Hello, world ({{ i }})
            |    {% endhello %}
            |  {% endfor %}
            |  {% hello "hello_tab_99" %}
            |    Hello, world (99)
            |  {% endhello %}
            |{% endhellos %}
        """.trimMargin()

        resource("homepage.peb", input.trim())

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    +"welcome yall"
                    ul {
                        li { +"yo, HELLO, WORLD (1)" }
                        li { +"yo, HELLO, WORLD (2)" }
                        li { +"yo, HELLO, WORLD (3)" }
                        li { +"yo, Hello, world (99)" }
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
                    +"howdy yall"
                    ul {
                        li { +"yo, Hello, world (3)" }
                        li { +"yo, Hello, world (6)" }
                        li { +"yo, Hello, world (7)" }
                    }
                }
            }
    }

    @Test
    fun testDynamicTabsWithNoLogic() {
        resource(
            "homepage.peb",
            """
            |{% hellos dynamic greeting='welcome' %}
            |   {% hello "hello_tab_2" :: upper %}
            |       Hello, world (2)
            |   {% endhello %}
            |   {% hello "hello_tab_3" %}
            |       Hello, world (3)
            |   {% endhello %}
            |   {% hello "hello_tab_4" :: upper %}
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
                    +"welcome yall"
                    ul {
                        li { +"yo, HELLO, WORLD (2)" }
                        li { +"yo, Hello, world (3)" }
                        li { +"yo, HELLO, WORLD (4)" }
                    }
                }
            }
    }

    @Test
    fun testDynamicTabsWithParentParams() {
        resource(
            "homepage.peb",
            """
            |{% hellos dynamic 'hello' closing=', and goodbye!' %}
            |   {% hello "hello_tab_2" %}
            |       Hello, world (2)
            |   {% endhello %}
            |   {% hello "hello_tab_3" %}
            |       Hello, world (3)
            |   {% endhello %}
            |   {% hello "hello_tab_4" :: upper %}
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
                    +"hello , and goodbye!"
                    ul {
                        li { +"yo, Hello, world (2)" }
                        li { +"yo, Hello, world (3)" }
                        li { +"yo, HELLO, WORLD (4)" }
                    }
                }
            }
    }

    @Test
    fun testDynamicTabsWithChildParams() {
        resource(
            "homepage.peb",
            """
            |{% hellos dynamic %}
            |   {% hello "hello_tab_2" tabGreeting='hey,' tabClosing=', wassup' %}
            |       Hello, world (2)
            |   {% endhello %}
            |   {% hello "hello_tab_3" tabGreeting='hi,' tabClosing=', wassup' %}
            |       Hello, world (3)
            |   {% endhello %}
            |   {% hello "hello_tab_4" tabGreeting='sup,' tabClosing=', wassup' :: upper %}
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
                    +"howdy yall"
                    ul {
                        li { +"hey, Hello, world (2) , wassup" }
                        li { +"hi, Hello, world (3) , wassup" }
                        li { +"sup, HELLO, WORLD (4) , wassup" }
                    }
                }
            }
    }

    @Test
    fun testDynamicTabsAlternateTemplate() {
        resource(
            "homepage.peb",
            """
            |{% hellos dynamic template='althellos' %}
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
                    +"alt howdy yall"
                    ul {
                        li { +"yo, Hello, world (3)" }
                        li { +"yo, Hello, world (6)" }
                        li { +"yo, Hello, world (7)" }
                    }
                }
            }
    }
}

class TabbedHellosTag : TemplateTag("hellos", Type.Tabbed, true) {

    @Option
    @StringDefault("howdy")
    lateinit var greeting: String

    @Option
    @StringDefault("yall")
    lateinit var closing: String

    override fun parameters() = arrayOf(::greeting.name, ::closing.name)

    override fun getNewTab(key: String?, content: String?): TemplateTag.Tab {
        return Tab(key, content)
    }

    class Tab(key: String?, content: String?) : TemplateTag.SimpleTab("hello", key, content) {

        @Option
        @StringDefault("yo,")
        lateinit var tabGreeting: String

        @Option
        lateinit var tabClosing: String

        override fun parameters() = arrayOf(::tabGreeting.name, ::tabClosing.name)
    }
}

@IgnoreModule
private class TabbedTagModule : OrchidModule() {
    override fun configure() {
        addToSet<TemplateTag, TabbedHellosTag>()
    }
}
