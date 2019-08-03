package com.eden.orchid.javadoc

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


@DisplayName("Tests page-rendering behavior of Javadoc generator")
class JavadocMenusTest : OrchidIntegrationTest(JavadocModule()) {

    @Test
    @DisplayName("The `javadocClassLinks` menu item creates anchor links to the top-level sections on a Class page")
    fun test01() {
        configObject(
            "javadoc",
            """
            |{
            |    "sourceDirs": "mockJava",
            |    "pages": {
            |        "extraCss": [
            |            "assets/css/orchidJavadoc.scss"
            |        ],
            |        "menu": [
            |            {
            |                "type": "javadocClassLinks"
            |            }
            |        ]
            |    }
            |}
            |""".trimMargin()
        )
        resource(
            "templates/pages/javadocClass.peb", """
            |{% include 'pageMenu' %}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/com/eden/orchid/mock/JavaClass/index.html") {
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
    @DisplayName("The `javadocClassLinks` can also include all the sub-items for each section")
    fun test02() {
        configObject(
            "javadoc",
            """
            |{
            |    "sourceDirs": "mockJava",
            |    "pages": {
            |        "extraCss": [
            |            "assets/css/orchidJavadoc.scss"
            |        ],
            |        "menu": [
            |            {
            |                "type": "javadocClassLinks",
            |                "includeItems": true
            |            }
            |        ]
            |    }
            |}
            |""".trimMargin()
        )
        resource(
            "templates/pages/javadocClass.peb", """
            |{% include 'pageMenu' %}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/com/eden/orchid/mock/JavaClass/index.html") {
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
                                        li { a(href = "#field__public_String_someData") { +"public String someData" } }
                                    }
                                }
                                li {
                                    +"Constructors"
                                    ul {
                                        li { a(href = "#constructor__public_JavaClass_String_s1_") { +"public JavaClass(String s1)" } }
                                    }
                                }
                                li {
                                    +"Methods"
                                    ul {
                                        li { a(href = "#method__public_String_doThing_String_s1_") { +"public String doThing(String s1)" } }
                                    }
                                }
                            }
                        }
                    }
            }
    }

    @Test
    @DisplayName("The `javadocClasses` menu item lists all classes")
    fun test03() {
        configObject(
            "javadoc",
            """
            |{
            |    "sourceDirs": "mockJava",
            |    "pages": {
            |        "extraCss": [
            |            "assets/css/orchidJavadoc.scss"
            |        ],
            |        "menu": [
            |            {
            |                "type": "javadocClasses"
            |            }
            |        ]
            |    }
            |}
            |""".trimMargin()
        )
        resource(
            "templates/pages/javadocClass.peb", """
            |{% include 'pageMenu' %}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/com/eden/orchid/mock/JavaClass/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches {
                            ul {
                                li {
                                    +"All Classes"
                                    ul {
                                        li { a(href = "http://orchid.test/com/eden/orchid/mock/JavaAnnotation") { +"JavaAnnotation" } }
                                        li { a(href = "http://orchid.test/com/eden/orchid/mock/JavaClass") { +"JavaClass" } }
                                        li { a(href = "http://orchid.test/com/eden/orchid/mock/JavaEnumClass") { +"JavaEnumClass" } }
                                        li { a(href = "http://orchid.test/com/eden/orchid/mock/JavaExceptionClass") { +"JavaExceptionClass" } }
                                        li { a(href = "http://orchid.test/com/eden/orchid/mock/JavaInterface") { +"JavaInterface" } }
                                    }
                                }
                            }
                        }
                    }
            }
    }

    @Test
    @DisplayName("The `javadocPackages` menu item lists all packages")
    fun test04() {
        configObject(
            "javadoc",
            """
            |{
            |    "sourceDirs": "mockJava",
            |    "pages": {
            |        "extraCss": [
            |            "assets/css/orchidJavadoc.scss"
            |        ],
            |        "menu": [
            |            {
            |                "type": "javadocPackages"
            |            }
            |        ]
            |    }
            |}
            |""".trimMargin()
        )
        resource(
            "templates/pages/javadocClass.peb", """
            |{% include 'pageMenu' %}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/com/eden/orchid/mock/JavaClass/index.html") {
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
