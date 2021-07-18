package com.eden.orchid.writersblocks.components

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.arr
import com.eden.orchid.testhelpers.obj
import com.eden.orchid.testhelpers.withGenerator
import com.eden.orchid.writersblocks.WritersBlocksModule
import kotlinx.html.iframe
import kotlinx.html.noScript
import kotlinx.html.script
import kotlinx.html.style
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

class GoogleTagManagerComponentTest : OrchidIntegrationTest(
    withGenerator<HomepageGenerator>(),
    WritersBlocksModule()
) {

    @Test
    @DisplayName("Google Tag Manager not added when not in production environment")
    fun test01() {
        resource(
            "homepage.md",
            """
            |---
            |metaComponents:
            |  - type: googleTagManager
            |    containerId: 'asdf'
            |---
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                }
            }
    }

    @Test
    @DisplayName("Google Tag Manager added in non-production environment with manual override")
    fun test02() {
        resource(
            "homepage.md",
            """
            |---
            |metaComponents:
            |  - type: googleTagManager
            |    containerId: 'asdf'
            |    productionOnly: 'false'
            |---
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    noScript {
                        iframe {
                            src = "https://www.googletagmanager.com/ns.html?id=asdf"
                            height = "0"
                            width = "0"
                            style = "display:none;visibility:hidden"
                        }
                    }
                    script {
                        +"""
                            |<!-- Google Tag Manager -->
                            |(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
                            |new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
                            |j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
                            |'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
                            |})(window,document,'script','dataLayer','asdf');
                            |<!-- End Google Tag Manager -->
                        """.trimMargin()
                    }
                }
            }
    }

    @Test
    @DisplayName("Google Tag Manager added in production environment")
    fun test03() {
        resource(
            "homepage.md",
            """
            |---
            |metaComponents:
            |  - type: googleTagManager
            |    containerId: 'asdf'
            |---
            """.trimMargin()
        )
        config {
            "site" to obj {
                "productionEnvironments" to arr {
                    add("test")
                }
            }
        }

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    noScript {
                        iframe {
                            src = "https://www.googletagmanager.com/ns.html?id=asdf"
                            height = "0"
                            width = "0"
                            style = "display:none;visibility:hidden"
                        }
                    }
                    script {
                        +"""
                            |<!-- Google Tag Manager -->
                            |(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
                            |new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
                            |j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
                            |'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
                            |})(window,document,'script','dataLayer','asdf');
                            |<!-- End Google Tag Manager -->
                        """.trimMargin()
                    }
                }
            }
    }
}
