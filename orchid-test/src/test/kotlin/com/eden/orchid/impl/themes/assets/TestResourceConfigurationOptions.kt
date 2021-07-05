package com.eden.orchid.impl.themes.assets

import com.eden.orchid.strikt.asExpected
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.htmlHeadMatches
import com.eden.orchid.strikt.pageWasNotRendered
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import kotlinx.html.div
import kotlinx.html.link
import kotlinx.html.script
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TestResourceConfigurationOptions : OrchidIntegrationTest(
    TestAssetsModule()
) {

    @BeforeEach
    fun setUp() {
        TestAssetsModule.addNormalAssetsToTest(this)
        flag("logLevel", "FATAL")
    }

    @Test
    @DisplayName("Test jsPage with module attr")
    fun test01() {
        flag("environment", "dev")
        configObject(
            "site",
            """
            |{
            |   "theme": "${TestAssetTheme.KEY}"
            |}
            """.trimMargin()
        )
        configObject(
            "allPages",
            """
            |{
            |   "extraCss": [
            |       {
            |           "asset": "https://copper-leaf.github.io/test-downloadable-assets/assets/css/style.css",
            |           "attrs": {
            |               "one": "1",
            |               "two": "2"
            |           }
            |       }
            |   ],
            |   "extraJs": [
            |       {
            |           "asset": "https://copper-leaf.github.io/test-downloadable-assets/assets/js/scripts.js",
            |           "attrs": {
            |               "one": "1",
            |               "two": "2"
            |           },
            |           "async": true,
            |           "defer": true,
            |           "module": true,
            |           "nomodule": false
            |       }
            |   ]
            |}
            """.trimMargin()
        )

        execute()
            .asExpected()
            .pageWasRendered("/test/asset/page-one/index.html") {
                htmlHeadMatches("head link[rel=stylesheet]") {
                    link(href = "http://orchid.test/TestAssetTheme/1e240/${TestAssetTheme.CSS}", rel = "stylesheet", type = "text/css") { }
                    link(href = "http://orchid.test/${TestAssetPage.CSS}", rel = "stylesheet", type = "text/css") { }
                    link(href = "https://copper-leaf.github.io/test-downloadable-assets/assets/css/style.css", rel = "stylesheet", type = "text/css") {
                        attributes["one"] = "1"
                        attributes["two"] = "2"
                    }
                }
                htmlBodyMatches("body script") {
                    script(src = "http://orchid.test/TestAssetTheme/1e240/${TestAssetTheme.JS}") { }
                    script(src = "http://orchid.test/${TestAssetPage.JS}") { }
                    script(src = "https://copper-leaf.github.io/test-downloadable-assets/assets/js/scripts.js") {
                        attributes["one"] = "1"
                        attributes["two"] = "2"
                        async = true
                        defer = true
                        type = "module"
                    }
                }
            }
            .pageWasNotRendered("/test-downloadable-assets/assets/css/style.css")
            .pageWasNotRendered("/test-downloadable-assets/assets/js/scripts.js")
    }

    @Test
    @DisplayName("Test jsPage with nomodule attr")
    fun test02() {
        flag("environment", "dev")
        configObject(
            "site",
            """
            |{
            |   "theme": "${TestAssetTheme.KEY}"
            |}
            """.trimMargin()
        )
        configObject(
            "allPages",
            """
            |{
            |   "extraJs": [
            |       {
            |           "asset": "https://copper-leaf.github.io/test-downloadable-assets/assets/js/scripts.js",
            |           "attrs": {
            |               "one": "1",
            |               "two": "2"
            |           },
            |           "async": true,
            |           "defer": true,
            |           "module": false,
            |           "nomodule": true
            |       }
            |   ]
            |}
            """.trimMargin()
        )

        execute()
            .asExpected()
            .pageWasRendered("/test/asset/page-one/index.html") {
                htmlBodyMatches("body script") {
                    script(src = "http://orchid.test/TestAssetTheme/1e240/${TestAssetTheme.JS}") { }
                    script(src = "http://orchid.test/${TestAssetPage.JS}") { }
                    script(src = "https://copper-leaf.github.io/test-downloadable-assets/assets/js/scripts.js") {
                        attributes["one"] = "1"
                        attributes["two"] = "2"
                        attributes["nomodule"] = ""
                        async = true
                        defer = true
                    }
                }
            }
            .pageWasNotRendered("/test-downloadable-assets/assets/css/style.css")
            .pageWasNotRendered("/test-downloadable-assets/assets/js/scripts.js")
    }
}
