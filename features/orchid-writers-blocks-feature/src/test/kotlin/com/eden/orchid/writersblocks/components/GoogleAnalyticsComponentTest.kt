package com.eden.orchid.writersblocks.components

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.arr
import com.eden.orchid.testhelpers.obj
import com.eden.orchid.testhelpers.withGenerator
import com.eden.orchid.writersblocks.WritersBlocksModule
import kotlinx.html.script
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

class GoogleAnalyticsComponentTest : OrchidIntegrationTest(
    withGenerator<HomepageGenerator>(),
    WritersBlocksModule()
) {

    @Test
    @DisplayName("Google Analytics not added when not in production environment")
    fun test01() {
        resource(
            "homepage.md",
            """
            |---
            |metaComponents:
            |  - type: googleAnalytics
            |    propertyId: 'asdf'
            |---
            """.trimMargin()
        )

        enableLogging()

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                }
            }
    }

    @Test
    @DisplayName("Google Analytics added in non-production environment with manual override")
    fun test02() {
        resource(
            "homepage.md",
            """
            |---
            |metaComponents:
            |  - type: googleAnalytics
            |    propertyId: 'asdf'
            |    productionOnly: 'false'
            |---
            """.trimMargin()
        )

        enableLogging()

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    script {
                        +"""
                            |<!-- Google Analytics -->
                            |(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
                            |    (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
                            |    m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
                            |})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');
                            |
                            |ga('create', 'asdf', 'auto');
                            |ga('send', 'pageview');
                            |<!-- End Google Analytics -->
                        """.trimMargin()
                    }
                }
            }
    }

    @Test
    @DisplayName("Google Analytics added in production environment")
    fun test03() {
        resource(
            "homepage.md",
            """
            |---
            |metaComponents:
            |  - type: googleAnalytics
            |    propertyId: 'asdf'
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

        enableLogging()

        expectThat(execute())
            .pageWasRendered("/index.html") {
                htmlBodyMatches {
                    script {
                        +"""
                            |<!-- Google Analytics -->
                            |(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
                            |    (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
                            |    m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
                            |})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');
                            |
                            |ga('create', 'asdf', 'auto');
                            |ga('send', 'pageview');
                            |<!-- End Google Analytics -->
                        """.trimMargin()
                    }
                }
            }
    }
}
