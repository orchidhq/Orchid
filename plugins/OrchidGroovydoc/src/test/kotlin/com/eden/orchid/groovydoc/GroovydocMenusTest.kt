package com.eden.orchid.groovydoc

import com.eden.orchid.strikt.asHtml
import com.eden.orchid.strikt.innerHtmlMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.strikt.select
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import kotlinx.html.a
import kotlinx.html.li
import kotlinx.html.ul
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat


@DisplayName("Tests page-rendering behavior of Groovydoc generator")
class GroovydocMenusTest : OrchidIntegrationTest(GroovydocModule()) {

    @Test
    @DisplayName("The `groovydocClassLinks` menu item creates anchor links to the top-level sections on a Class page")
    fun test01() {
        configObject(
            "groovydoc",
            """
            |{
            |    "sourceDirs": "mockGroovy",
            |    "pages": {
            |        "extraCss": [
            |            "assets/css/orchidGroovydoc.scss"
            |        ],
            |        "menu": [
            |            {
            |                "type": "groovydocClassLinks"
            |            }
            |        ]
            |    }
            |}
            |""".trimMargin()
        )
        resource(
            "templates/pages/groovydocClass.peb", """
            |{% include 'pageMenu' %}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/com/eden/orchid/mock/GroovyClass/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches {
                            ul {
                                li { a(href = "#summary") { +"Summary" } }
                                li { a(href = "#description") { +"Description" } }
                                li { a(href = "#fields") { +"Fields" } }
                                li { a(href = "#constructors") { +"Constructors" } }
                                li { a(href = "#methods") { +"Methods" } }
                            }
                        }
                    }
            }
    }

    @Test
    @DisplayName("The `groovydocClassLinks` can also include all the sub-items for each section")
    fun test02() {
        configObject(
            "groovydoc",
            """
            |{
            |    "sourceDirs": "mockGroovy",
            |    "pages": {
            |        "extraCss": [
            |            "assets/css/orchidGroovydoc.scss"
            |        ],
            |        "menu": [
            |            {
            |                "type": "groovydocClassLinks",
            |                "includeItems": true
            |            }
            |        ]
            |    }
            |}
            |""".trimMargin()
        )
        resource(
            "templates/pages/groovydocClass.peb", """
            |{% include 'pageMenu' %}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/com/eden/orchid/mock/GroovyClass/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches {
                            ul {
                                li { a(href = "#summary") { +"Summary" } }
                                li { a(href = "#description") { +"Description" } }
                                li {
                                    +"Fields"
                                    ul {
                                        li { a(href = "#field__String_someData") { +"String someData" } }
                                    }
                                }
                                li {
                                    +"Constructors"
                                    ul {
                                        li { a(href = "#constructor__GroovyClass_String_s1_") { +"GroovyClass(String s1)" } }
                                    }
                                }
                                li {
                                    +"Methods"
                                    ul {
                                        li { a(href = "#method__String_doThing_String_s1_") { +"String doThing(String s1)" } }
                                    }
                                }
                            }
                        }
                    }
            }
    }

    @Test
    @DisplayName("The `groovydocClasses` menu item lists all classes")
    fun test03() {
        configObject(
            "groovydoc",
            """
            |{
            |    "sourceDirs": "mockGroovy",
            |    "pages": {
            |        "extraCss": [
            |            "assets/css/orchidGroovydoc.scss"
            |        ],
            |        "menu": [
            |            {
            |                "type": "groovydocClasses"
            |            }
            |        ]
            |    }
            |}
            |""".trimMargin()
        )
        resource(
            "templates/pages/groovydocClass.peb", """
            |{% include 'pageMenu' %}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/com/eden/orchid/mock/GroovyClass/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches {
                            ul {
                                li {
                                    +"All Classes"
                                    ul {
                                        li { a(href = "http://orchid.test/com/eden/orchid/mock/GroovyAnnotation") { +"GroovyAnnotation" } }
                                        li { a(href = "http://orchid.test/com/eden/orchid/mock/GroovyClass") { +"GroovyClass" } }
                                        li { a(href = "http://orchid.test/com/eden/orchid/mock/GroovyEnumClass") { +"GroovyEnumClass" } }
                                        li { a(href = "http://orchid.test/com/eden/orchid/mock/GroovyExceptionClass") { +"GroovyExceptionClass" } }
                                        li { a(href = "http://orchid.test/com/eden/orchid/mock/GroovyInterface") { +"GroovyInterface" } }
                                        li { a(href = "http://orchid.test/com/eden/orchid/mock/GroovyTrait") { +"GroovyTrait" } }
                                    }
                                }
                            }
                        }
                    }
            }
    }

    @Test
    @DisplayName("The `groovydocPackages` menu item lists all packages")
    fun test04() {
        configObject(
            "groovydoc",
            """
            |{
            |    "sourceDirs": "mockGroovy",
            |    "pages": {
            |        "extraCss": [
            |            "assets/css/orchidGroovydoc.scss"
            |        ],
            |        "menu": [
            |            {
            |                "type": "groovydocPackages"
            |            }
            |        ]
            |    }
            |}
            |""".trimMargin()
        )
        resource(
            "templates/pages/groovydocClass.peb", """
            |{% include 'pageMenu' %}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/com/eden/orchid/mock/GroovyClass/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches {
                            ul {
                                li {
                                    +"All Packages"
                                    ul {
                                        li { a(href = "http://orchid.test/com/eden/orchid/mock") { +"com.eden.orchid.mock" } }
                                    }
                                }
                            }
                        }
                    }
            }
    }

}
