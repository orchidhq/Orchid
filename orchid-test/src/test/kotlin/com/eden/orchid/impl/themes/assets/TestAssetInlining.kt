package com.eden.orchid.impl.themes.assets

import com.eden.orchid.strikt.asExpected
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.htmlHeadMatches
import com.eden.orchid.strikt.pageWasNotRendered
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.arr
import com.eden.orchid.testhelpers.obj
import kotlinx.html.div
import kotlinx.html.link
import kotlinx.html.script
import kotlinx.html.style
import kotlinx.html.unsafe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TestAssetInlining : OrchidIntegrationTest(
    TestAssetsModule()
) {

    @BeforeEach
    fun setUp() {
        TestAssetsModule.addNormalAssetsToTest(this)
        flag("logLevel", "FATAL")
        flag("diagnose", true)
        resource(
            "assets/css/inlinedCss.scss",
            """
            |.red {
            |   .blue {
            |       color: purple;
            |   }
            |}
            """.trimMargin()
        )
        resource(
            "assets/js/inlinedJs.js",
            """
            |function itsFunToInline() {
            |
            |}
            """.trimMargin()
        )
    }

    @Test
    @DisplayName("Test normal assets are inlined into page")
    fun test01() {
        config {
            "site" to obj {
                "theme" to TestAssetTheme.KEY
            }
            "allPages" to obj {
                "components" to arr {
                    this add obj {
                        "type" to "pageContent"
                        "noWrapper" to false
                    }
                }
                "extraCss" to arr {
                    this add obj {
                        "asset" to "assets/css/inlinedCss.scss"
                        "inlined" to true
                    }
                }
                "extraJs" to arr {
                    this add obj {
                        "asset" to "assets/js/inlinedJs.js"
                        "inlined" to true
                    }
                }
            }
        }

        execute()
            .asExpected()
            .pageWasRendered("/test/asset/page-one/index.html") {
                htmlHeadMatches("head link[rel=stylesheet], head style", ignoreContentWhitespace = true) {
                    link(
                        href = "http://orchid.test/TestAssetTheme/1e240/${TestAssetTheme.CSS}",
                        rel = "stylesheet",
                        type = "text/css"
                    ) { }
                    link(href = "http://orchid.test/${TestAssetPage.CSS}", rel = "stylesheet", type = "text/css") { }
                    style {
                        unsafe { +".red .blue { color: purple; }" }
                    }
                }
                htmlBodyMatches("body script", ignoreContentWhitespace = true) {
                    script(src = "http://orchid.test/TestAssetTheme/1e240/${TestAssetTheme.JS}") { }
                    script(src = "http://orchid.test/${TestAssetPage.JS}") { }
                    script {
                        unsafe { +"function itsFunToInline() { }" }
                    }
                }
            }
            .pageWasNotRendered("/assets/css/inlinedCss.scss")
            .pageWasNotRendered("/assets/js/inlinedJs.js")
    }

    @Test
    @DisplayName("Test external assets not downloaded or inlined in dev environment")
    fun test02() {
        config {
            "site" to obj {
                "theme" to TestAssetTheme.KEY
            }
            "allPages" to obj {
                "extraCss" to arr {
                    this add obj {
                        "asset" to "https://copper-leaf.github.io/test-downloadable-assets/assets/css/style.css"
                        "download" to true
                        "inlined" to true
                    }
                }
                "extraJs" to arr {
                    this add obj {
                        "asset" to "https://copper-leaf.github.io/test-downloadable-assets/assets/js/scripts.js"
                        "download" to true
                        "inlined" to true
                    }
                }
            }
        }

        execute()
            .asExpected()
            .pageWasRendered("/test/asset/page-one/index.html") {
                htmlHeadMatches("head link[rel=stylesheet]", ignoreContentWhitespace = true) {
                    link(
                        href = "http://orchid.test/TestAssetTheme/1e240/${TestAssetTheme.CSS}",
                        rel = "stylesheet",
                        type = "text/css"
                    ) { }
                    link(href = "http://orchid.test/${TestAssetPage.CSS}", rel = "stylesheet", type = "text/css") { }
                    link(
                        href = "https://copper-leaf.github.io/test-downloadable-assets/assets/css/style.css",
                        rel = "stylesheet",
                        type = "text/css"
                    ) { }
                }
                htmlBodyMatches("body script", ignoreContentWhitespace = true) {
                    script(src = "http://orchid.test/TestAssetTheme/1e240/${TestAssetTheme.JS}") { }
                    script(src = "http://orchid.test/${TestAssetPage.JS}") { }
                    script(src = "https://copper-leaf.github.io/test-downloadable-assets/assets/js/scripts.js") { }
                }
            }
            .pageWasNotRendered("/test-downloadable-assets/assets/css/style.css")
            .pageWasNotRendered("/test-downloadable-assets/assets/js/scripts.js")
    }

    @Test
    @DisplayName("Test external assets downloaded and inlined in prod environment")
    fun test03() {
        flag("environment", "prod")
        config {
            "site" to obj {
                "theme" to TestAssetTheme.KEY
            }
            "allPages" to obj {
                "extraCss" to arr {
                    this add obj {
                        "asset" to "https://copper-leaf.github.io/test-downloadable-assets/assets/css/style.css"
                        "download" to true
                        "inlined" to true
                    }
                }
                "extraJs" to arr {
                    this add obj {
                        "asset" to "https://copper-leaf.github.io/test-downloadable-assets/assets/js/scripts.js"
                        "download" to true
                        "inlined" to true
                    }
                }
            }
        }

        execute()
            .asExpected()
            .pageWasRendered("/test/asset/page-one/index.html") {
                htmlHeadMatches("head link[rel=stylesheet], head style", ignoreContentWhitespace = true) {
                    link(
                        href = "http://orchid.test/TestAssetTheme/1e240/${TestAssetTheme.CSS}",
                        rel = "stylesheet",
                        type = "text/css"
                    ) { }
                    link(href = "http://orchid.test/${TestAssetPage.CSS}", rel = "stylesheet", type = "text/css") { }
                    style {
                        unsafe {
                            +"""
                            |.extra-css {
                            |  color: blue;
                            |}
                            """.trimMargin()
                        }
                    }
                }
                htmlBodyMatches("body script", ignoreContentWhitespace = true) {
                    script(src = "http://orchid.test/TestAssetTheme/1e240/${TestAssetTheme.JS}") { }
                    script(src = "http://orchid.test/${TestAssetPage.JS}") { }
                    script {
                        unsafe {
                            +"""
                            |fun js() {
                            |    console.log("js");
                            |}
                            """.trimMargin()
                        }
                    }
                }
            }
            .pageWasNotRendered("/test-downloadable-assets/assets/css/style.css")
            .pageWasNotRendered("/test-downloadable-assets/assets/js/scripts.js")
    }
}
