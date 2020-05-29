package com.eden.orchid.languages.asciidoc

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import kotlinx.html.div
import kotlinx.html.p
import kotlinx.html.strong
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

@DisplayName("Tests behavior of using Asciidoc for the homepage")
class AsciidocTest : OrchidIntegrationTest(withGenerator<HomepageGenerator>()) {

    @Test
    @DisplayName("Test that Markdown works normally")
    fun test01() {
        resource(
            "homepage.md",
            """
            |**Markdown Page**
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    p {
                        strong {
                            +"Markdown Page"
                        }
                    }
                }
            }
    }

    @Test
    @DisplayName("Test that Asciidoc syntax is not supported when the module is not included. Homepage file will not be found at all.")
    fun test02() {
        resource(
            "homepage.ad",
            """
            |**Unknown Asciidoc Page**
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    +""
                }
            }
    }

    @Test
    @DisplayName("Test that Asciidoc syntax works when the file ends with .ad when the module is included")
    fun test03() {
        resource(
            "homepage.ad",
            """
            |**Asciidoc Page**
            """.trimMargin()
        )

        expectThat(execute(AsciidocModule()))
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    div("paragraph") {
                        p {
                            strong {
                                +"Asciidoc Page"
                            }
                        }
                    }
                }
            }
    }

// Test safe mode configuration
//----------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Test that Asciidoc fallback Orchid resource includes are enabled with secure safe-mode.")
    fun test05() {
        resource(
            "homepage.ad",
            """
            |**Asciidoc Page**
            |
            |include::secure_include_resource.ad[]
            """.trimMargin()
        )
        resource(
            "secure_include_resource.ad",
            """
            |Included from resource
            """.trimMargin()
        )
        configObject(
            "services",
            """
            {
                "compilers": {
                    "adoc": {
                        "safeMode": "SECURE"
                    }
                }
            }
            """.trimIndent()
        )

        expectThat(execute(AsciidocModule()))
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    div("paragraph") {
                        p {
                            strong {
                                +"Asciidoc Page"
                            }
                        }
                    }
                    // when secure, the contents are not loaded
                    div("paragraph") {
                        p {
                            +"Included from resource"
                        }
                    }
                }
            }
    }

    @Test
    @DisplayName("Test that Asciidoc native file includes are enabled with default safe-mode.")
    fun test06() {
        resource(
            "homepage.ad",
            """
            |**Asciidoc Page**
            |
            |include::test/resources/secure_include.ad[]
            """.trimMargin()
        )

        expectThat(execute(AsciidocModule()))
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    div("paragraph") {
                        p {
                            strong {
                                +"Asciidoc Page"
                            }
                        }
                    }
                    div("paragraph") {
                        p {
                            +"Included from file"
                        }
                    }
                }
            }
    }

    @Test
    @DisplayName("Test that Asciidoc fallback Orchid resource includes are enabled with secure safe-mode.")
    fun test07() {
        resource(
            "homepage.ad",
            """
            |**Asciidoc Page**
            |
            |include::secure_include_resource.ad[]
            """.trimMargin()
        )
        resource(
            "secure_include_resource.ad",
            """
            |Included from resource
            """.trimMargin()
        )

        expectThat(execute(AsciidocModule()))
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    div("paragraph") {
                        p {
                            strong {
                                +"Asciidoc Page"
                            }
                        }
                    }
                    // when secure, the contents are not loaded
                    div("paragraph") {
                        p {
                            +"Included from resource"
                        }
                    }
                }
            }
    }

    @Test
    @DisplayName("Test that Asciidoc partial includes work properly with `tag` attribute")
    fun test08() {
        resource(
            "homepage.ad",
            """
            |**Asciidoc Page**
            |
            |before included [tag=includedBlock1]
            |
            |include::test/resources/included_tags.ad[tag=includedBlock1]
            |
            |after included [tag=includedBlock1]
            |
            |before included [tag=includedBlock2]
            |
            |include::test/resources/included_tags.ad[tag=includedBlock2]
            |
            |after included [tag=includedBlock2]
            """.trimMargin()
        )

        expectThat(execute(AsciidocModule()))
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    div("paragraph") {
                        p {
                            strong { +"Asciidoc Page" }
                        }
                    }
                    div("paragraph") {
                        p { +"before included [tag=includedBlock1]" }
                    }
                    div("paragraph") {
                        p { +"Content in block 1" }
                    }
                    div("paragraph") {
                        p { +"after included [tag=includedBlock1]" }
                    }

                    div("paragraph") {
                        p { +"before included [tag=includedBlock2]" }
                    }
                    div("paragraph") {
                        p { +"Content in block 2" }
                    }
                    div("paragraph") {
                        p { +"after included [tag=includedBlock2]" }
                    }
                }
            }
    }

    @Test
    @DisplayName("Test that Asciidoc partial includes work properly with `tags` attribute")
    fun test09() {
        resource(
            "homepage.ad",
            """
            |**Asciidoc Page**
            |
            |before included [tags=includedBlock1;includedBlock2]
            |
            |include::test/resources/included_tags.ad[tags=includedBlock1;includedBlock2]
            |
            |after included [tags=includedBlock1;includedBlock2]
            |
            |before included [tags=*]
            |
            |include::test/resources/included_tags.ad[tags=*]
            |
            |after included [tags=*]
            """.trimMargin()
        )

        expectThat(execute(AsciidocModule()))
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    div("paragraph") {
                        p {
                            strong { +"Asciidoc Page" }
                        }
                    }
                    div("paragraph") {
                        p { +"before included [tags=includedBlock1;includedBlock2]" }
                    }
                    div("paragraph") {
                        p { +"Content in block 1 Content in block 2" }
                    }
                    div("paragraph") {
                        p { +"after included [tags=includedBlock1;includedBlock2]" }
                    }

                    div("paragraph") {
                        p { +"before included [tags=*]" }
                    }
                    div("paragraph") {
                        p { +"Content in block 1 Content in block 2" }
                    }
                    div("paragraph") {
                        p { +"after included [tags=*]" }
                    }
                }
            }
    }

    @Test
    @DisplayName("Test that Asciidoc partial includes work properly with `lines` attribute")
    fun test10() {
        resource(
            "homepage.ad",
            """
            |**Asciidoc Page**
            |
            |before included [lines=1..-1]
            |
            |include::test/resources/included_tags.ad[lines=1..-1]
            |
            |after included [lines=1..-1]
            |
            |before included [lines=5..7]
            |
            |include::test/resources/included_tags.ad[lines=5..7]
            |
            |after included [lines=5..7]
            |
            |before included [lines=5;6..7]
            |
            |include::test/resources/included_tags.ad[lines=5;6..7]
            |
            |after included [lines=5;6..7]
            |
            |before included [lines='5,6..7']
            |
            |include::test/resources/included_tags.ad[lines=5;6..7]
            |
            |after included [lines='5,6..7']
            """.trimMargin()
        )

        expectThat(execute(AsciidocModule()))
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    div("paragraph") {
                        p {
                            strong { +"Asciidoc Page" }
                        }
                    }
                    div("paragraph") {
                        p { +"before included [lines=1..-1]" }
                    }

                    div("paragraph") {
                        p { +"tag::includedBlock1[] Content in block 1 end::includedBlock1[]" }
                    }
                    div("paragraph") {
                        p { +"tag::includedBlock2[] Content in block 2 end::includedBlock2[]" }
                    }
                    div("paragraph") {
                        p { +"after included [lines=1..-1]" }
                    }

                    div("paragraph") {
                        p { +"before included [lines=5..7]" }
                    }
                    div("paragraph") {
                        p { +"tag::includedBlock2[] Content in block 2 end::includedBlock2[]" }
                    }
                    div("paragraph") {
                        p { +"after included [lines=5..7]" }
                    }

                    div("paragraph") {
                        p { +"before included [lines=5;6..7]" }
                    }
                    div("paragraph") {
                        p { +"tag::includedBlock2[] Content in block 2 end::includedBlock2[]" }
                    }
                    div("paragraph") {
                        p { +"after included [lines=5;6..7]" }
                    }

                    div("paragraph") {
                        p { +"before included [lines='5,6..7']" }
                    }
                    div("paragraph") {
                        p { +"tag::includedBlock2[] Content in block 2 end::includedBlock2[]" }
                    }
                    div("paragraph") {
                        p { +"after included [lines='5,6..7']" }
                    }
                }
            }
    }

    @Test
    @DisplayName("Test that resource includes are safely handled when the resource does not exist.")
    fun test11() {
        resource(
            "homepage.ad",
            """
            |**Asciidoc Page**
            |
            |include::missing_resource.ad[]
            """.trimMargin()
        )

        expectThat(execute(AsciidocModule()))
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    div("paragraph") {
                        p {
                            strong {
                                +"Asciidoc Page"
                            }
                        }
                    }
                }
            }
    }

}
