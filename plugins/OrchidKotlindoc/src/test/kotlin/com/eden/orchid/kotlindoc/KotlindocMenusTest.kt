package com.eden.orchid.kotlindoc

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

@DisplayName("Tests page-rendering behavior of Kotlindoc generator")
class KotlindocMenusTest : OrchidIntegrationTest(KotlindocModule()) {

    @Test
    @DisplayName("The `kotlindocClassLinks` menu item creates anchor links to the top-level sections on a Class page")
    fun test01() {
        configObject(
            "kotlindoc",
            """
            |{
            |    "sourceDirs": "mockKotlin",
            |    "pages": {
            |        "extraCss": [
            |            "assets/css/orchidKotlindoc.scss"
            |        ],
            |        "menu": [
            |            {
            |                "type": "kotlindocClassLinks"
            |            }
            |        ]
            |    }
            |}
            |""".trimMargin()
        )
        resource(
            "templates/pages/kotlindocClass.peb", """
            |{% include 'pageMenu' %}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/com/eden/orchid/mock/KotlinClass/index.html") {
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
    @DisplayName("The `kotlindocClassLinks` can also include all the sub-items for each section")
    fun test02() {
        configObject(
            "kotlindoc",
            """
            |{
            |    "sourceDirs": "mockKotlin",
            |    "pages": {
            |        "extraCss": [
            |            "assets/css/orchidKotlindoc.scss"
            |        ],
            |        "menu": [
            |            {
            |                "type": "kotlindocClassLinks",
            |                "includeItems": true
            |            }
            |        ]
            |    }
            |}
            |""".trimMargin()
        )
        resource(
            "templates/pages/kotlindocClass.peb", """
            |{% include 'pageMenu' %}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/com/eden/orchid/mock/KotlinClass/index.html") {
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
                                        li { a(href = "#field__var_someData__String") { +"var someData: String" } }
                                    }
                                }
                                li {
                                    +"Constructors"
                                    ul {
                                        li { a(href = "#constructor__constructor_s1__String_") { +"constructor(s1: String)" } }
                                    }
                                }
                                li {
                                    +"Methods"
                                    ul {
                                        li { a(href = "#method__fun_doThing_s1__String___String") { +"fun doThing(s1: String): String" } }
                                    }
                                }
                            }
                        }
                    }
            }
    }

    @Test
    @DisplayName("The `kotlindocClasses` menu item lists all classes")
    fun test03() {
        configObject(
            "kotlindoc",
            """
            |{
            |    "sourceDirs": "mockKotlin",
            |    "pages": {
            |        "extraCss": [
            |            "assets/css/orchidKotlindoc.scss"
            |        ],
            |        "menu": [
            |            {
            |                "type": "kotlindocClasses"
            |            }
            |        ]
            |    }
            |}
            |""".trimMargin()
        )
        resource(
            "templates/pages/kotlindocClass.peb", """
            |{% include 'pageMenu' %}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/com/eden/orchid/mock/KotlinClass/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches {
                            ul {
                                li {
                                    +"All Classes"
                                    ul {
                                        li { a(href = "http://orchid.test/com/eden/orchid/mock/CustomString") { +"CustomString" } }
                                        li { a(href = "http://orchid.test/com/eden/orchid/mock/KotlinAnnotation") { +"KotlinAnnotation" } }
                                        li { a(href = "http://orchid.test/com/eden/orchid/mock/KotlinClass") { +"KotlinClass" } }
                                        li { a(href = "http://orchid.test/com/eden/orchid/mock/KotlinClassWithCompanionObject") { +"KotlinClassWithCompanionObject" } }
                                        li { a(href = "http://orchid.test/com/eden/orchid/mock/KotlinEnumClass") { +"KotlinEnumClass" } }
                                        li { a(href = "http://orchid.test/com/eden/orchid/mock/KotlinExceptionClass") { +"KotlinExceptionClass" } }
                                        li { a(href = "http://orchid.test/com/eden/orchid/mock/KotlinInlineClass") { +"KotlinInlineClass" } }
                                        li { a(href = "http://orchid.test/com/eden/orchid/mock/KotlinInterface") { +"KotlinInterface" } }
                                        li { a(href = "http://orchid.test/com/eden/orchid/mock/KotlinObjectClass") { +"KotlinObjectClass" } }
                                        li { a(href = "http://orchid.test/com/eden/orchid/mock/KotlinSealedClass1") { +"KotlinSealedClass1" } }
                                        li { a(href = "http://orchid.test/com/eden/orchid/mock/KotlinSealedClass2") { +"KotlinSealedClass2" } }
                                        li { a(href = "http://orchid.test/com/eden/orchid/mock/KotlinSealedClass3") { +"KotlinSealedClass3" } }
                                        li { a(href = "http://orchid.test/com/eden/orchid/mock/KotlinSealedClasses") { +"KotlinSealedClasses" } }
                                    }
                                }
                            }
                        }
                    }
            }
    }

    @Test
    @DisplayName("The `kotlindocPackages` menu item lists all packages")
    fun test04() {
        configObject(
            "kotlindoc",
            """
            |{
            |    "sourceDirs": "mockKotlin",
            |    "pages": {
            |        "extraCss": [
            |            "assets/css/orchidKotlindoc.scss"
            |        ],
            |        "menu": [
            |            {
            |                "type": "kotlindocPackages"
            |            }
            |        ]
            |    }
            |}
            |""".trimMargin()
        )
        resource(
            "templates/pages/kotlindocClass.peb", """
            |{% include 'pageMenu' %}
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/com/eden/orchid/mock/KotlinClass/index.html") {
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
