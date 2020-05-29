package com.eden.orchid.javadoc

import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import kotlinx.html.a
import kotlinx.html.li
import kotlinx.html.ul
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.DisabledForJreRange
import org.junit.jupiter.api.condition.JRE
import strikt.api.expectThat

@DisplayName("Tests page-rendering behavior of Javadoc generator")
@DisabledForJreRange(min = JRE.JAVA_12)
class LegacyJavadocMenusTest : OrchidIntegrationTest(JavadocModule()) {

    @BeforeEach
    fun setUp() {
        flag("legacySourceDoc", true)
    }

    @Test
    @DisplayName("The `javadocClassLinks` menu item creates anchor links to the top-level sections on a Class page")
    fun test01() {
        configObject(
            "javadoc",
            """
            |{
            |    "sourceDirs": "mockJava",
            |    "showRunnerLogs": true,
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
                htmlBodyMatches {
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

    @Test
    @DisplayName("The `javadocClassLinks` can also include all the sub-items for each section")
    fun test02() {
        configObject(
            "javadoc",
            """
            |{
            |    "sourceDirs": "mockJava",
            |    "showRunnerLogs": true,
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
                htmlBodyMatches {
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
                                li { a(href = "#method__public_String_methodWithVarargs_String_strings_") { +"public String methodWithVarargs(String strings)" } }
                                li { a(href = "#method__public_String_methodWithStringArray_String_strings_") { +"public String methodWithStringArray(String strings)" } }
                                li { a(href = "#method__public_String_methodWithMultiDimensionStringArray_String_strings_") { +"public String methodWithMultiDimensionStringArray(String strings)" } }
                                li { a(href = "#method__public_String_methodWithMultiDimensionStringArrayWithVararg_String_strings_") { +"public String methodWithMultiDimensionStringArrayWithVararg(String strings)" } }
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
            |    "showRunnerLogs": true,
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
                htmlBodyMatches {
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

    @Test
    @DisplayName("The `javadocPackages` menu item lists all packages")
    fun test04() {
        configObject(
            "javadoc",
            """
            |{
            |    "sourceDirs": "mockJava",
            |    "showRunnerLogs": true,
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
                htmlBodyMatches {
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
