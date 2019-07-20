package com.eden.orchid.languages.bible

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.asHtml
import com.eden.orchid.strikt.innerHtmlMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.strikt.select
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import kotlinx.html.id
import kotlinx.html.p
import kotlinx.html.span
import kotlinx.html.sup
import kotlinx.html.unsafe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import strikt.api.expectThat

@DisplayName("Tests behavior of using Bible verse functions")
class BibleTest : OrchidIntegrationTest(withGenerator<HomepageGenerator>()) {

    @Test
    @DisplayName("Test that the bible verse function works using the default version")
    @EnabledIfEnvironmentVariable(named = "absApiKey", matches = ".+")
    fun test01() {
        resource("homepage.peb", "{{ bible('John 3:16') }}")

        expectThat(execute(BibleModule()))
            .pageWasRendered("/index.html") {
                get { content }
                    .asHtml()
                    .select("body") {
                        innerHtmlMatches {
                            p("p") {
                                sup("v") {
                                    id = "John.3.16"
                                    +"16"
                                }
                                span("wj") {
                                    +"“For God so loved the world, that He gave His only begotten Son, that whoever believes in Him shall not perish, but have eternal life."
                                }
                            }
                            +" ~ John 3:16"
                        }
                    }
            }
    }

    @Test
    @DisplayName("Test that the bible verse function works using a different default version")
    @EnabledIfEnvironmentVariable(named = "absApiKey", matches = ".+")
    fun test02() {
        flag("absDefaultVersion", "eng-KJV")
        resource("homepage.peb", "{{ bible('John 3:16') }}")

        expectThat(execute(BibleModule()))
            .pageWasRendered("/index.html") {
                get { content }
                    .asHtml(true)
                    .select("body") {
                        innerHtmlMatches {
                            p("p") {
                                sup("v") {
                                    id = "John.3.16"
                                    +"16"
                                }
                                +"For God so loved the world, that he gave his only begotten Son, that whosoever believeth in him should not perish, but have everlasting life."
                            }
                            +" ~ John 3:16"
                        }
                    }
            }
    }

    @Test
    @DisplayName("Test that the bible verse function works specifying a version in the function call")
    @EnabledIfEnvironmentVariable(named = "absApiKey", matches = ".+")
    fun test03() {
        resource("homepage.peb", "{{ bible('John 3:16', 'eng-AMP') }}")

        expectThat(execute(BibleModule()))
            .pageWasRendered("/index.html") {
                get { content }
                    .asHtml(true)
                    .select("body") {
                        innerHtmlMatches {
                            p("p") {
                                sup("v") {
                                    id = "John.3.16"
                                    +"16"
                                }
                                unsafe { +"For God so greatly loved\n  <span class=\"it\">and</span>\ndearly prized the world that He [even] gave up His only begotten (unique) Son, so that whoever believes in (trusts in, clings to, relies on) Him shall not perish (come to destruction, be lost) but have eternal (everlasting) life." }
                            }
                            +" ~ John 3:16"
                        }
                    }
            }
    }

}
