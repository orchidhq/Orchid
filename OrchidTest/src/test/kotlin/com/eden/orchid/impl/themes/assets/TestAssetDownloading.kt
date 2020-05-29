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
import strikt.assertions.isEqualTo

class TestAssetDownloading : OrchidIntegrationTest(
    TestAssetsModule()
) {

    @BeforeEach
    fun setUp() {
        TestAssetsModule.addNormalAssetsToTest(this)
        flag("logLevel", "FATAL")
    }

    @Test
    @DisplayName("Test external assets not downloaded in dev environment")
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
            |   "components": [
            |       {"type": "pageContent", "noWrapper": false}
            |   ],
            |   "extraCss": [
            |       "https://copper-leaf.github.io/test-downloadable-assets/assets/css/style.css"
            |   ],
            |   "extraJs": [
            |       "https://copper-leaf.github.io/test-downloadable-assets/assets/js/scripts.js"
            |   ]
            |}
            """.trimMargin()
        )

        execute()
            .asExpected()
            .pageWasRendered("/test/asset/page-one/index.html") {
                htmlHeadMatches("head link[rel=stylesheet]") {
                    link(href="http://orchid.test/TestAssetTheme/1e240/${TestAssetTheme.CSS}", rel="stylesheet", type="text/css") { }
                    link(href="http://orchid.test/${TestAssetPage.CSS}", rel="stylesheet", type="text/css") { }
                    link(href="https://copper-leaf.github.io/test-downloadable-assets/assets/css/style.css", rel="stylesheet", type="text/css") { }
                }
                htmlBodyMatches {
                    div("component component-pageContent component-order-0") {}
                    script(src="http://orchid.test/TestAssetTheme/1e240/${TestAssetTheme.JS}") { }
                    script(src="http://orchid.test/${TestAssetPage.JS}") { }
                    script(src="https://copper-leaf.github.io/test-downloadable-assets/assets/js/scripts.js") { }
                }
            }
            .pageWasNotRendered("/test-downloadable-assets/assets/css/style.css")
            .pageWasNotRendered("/test-downloadable-assets/assets/js/scripts.js")
    }

    @Test
    @DisplayName("Test external assets are downloaded in prod environment")
    fun test02() {
        flag("environment", "prod")
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
            |   "components": [
            |       {"type": "pageContent", "noWrapper": false}
            |   ],
            |   "extraCss": [
            |       "https://copper-leaf.github.io/test-downloadable-assets/assets/css/style.css"
            |   ],
            |   "extraJs": [
            |       "https://copper-leaf.github.io/test-downloadable-assets/assets/js/scripts.js"
            |   ]
            |}
            """.trimMargin()
        )

        execute()
            .asExpected()
            .pageWasRendered("/test/asset/page-one/index.html") {
                htmlHeadMatches("head link[rel=stylesheet]") {
                    link(href="http://orchid.test/TestAssetTheme/1e240/${TestAssetTheme.CSS}", rel="stylesheet", type="text/css") { }
                    link(href="http://orchid.test/${TestAssetPage.CSS}", rel="stylesheet", type="text/css") { }
                    link(href="http://orchid.test/test-downloadable-assets/assets/css/style.css", rel="stylesheet", type="text/css") { }
                }
                htmlBodyMatches {
                    div("component component-pageContent component-order-0") {}
                    script(src="http://orchid.test/TestAssetTheme/1e240/${TestAssetTheme.JS}") { }
                    script(src="http://orchid.test/${TestAssetPage.JS}") { }
                    script(src="http://orchid.test/test-downloadable-assets/assets/js/scripts.js") { }
                }
            }
            .pageWasRendered("/test-downloadable-assets/assets/css/style.css") {
                get { content.replace("\\s+".toRegex(), "") }
                    .isEqualTo(
                        """
                        |.extra-css {
                        |  color: blue;
                        |}
                        """.trimMargin().replace("\\s+".toRegex(), "")
                    )
            }
            .pageWasRendered("/test-downloadable-assets/assets/js/scripts.js") {
                get { content.replace("\\s+".toRegex(), "") }
                    .isEqualTo(
                        """
                        |fun js() {
                        |    console.log("js");
                        |}
                        """.trimMargin().replace("\\s+".toRegex(), "")
                    )
            }
    }

    @Test
    @DisplayName("Test external assets are not downloaded in prod environment when `download` is set to `false`")
    fun test03() {
        flag("environment", "prod")
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
            |   "components": [
            |       {"type": "pageContent", "noWrapper": false}
            |   ],
            |   "extraCss": [
            |       {
            |           "asset": "https://copper-leaf.github.io/test-downloadable-assets/assets/css/style.css",
            |           "download": false
            |       }
            |   ],
            |   "extraJs": [
            |       {
            |           "asset": "https://copper-leaf.github.io/test-downloadable-assets/assets/js/scripts.js",
            |           "download": false
            |       }
            |   ]
            |}
            """.trimMargin()
        )

        execute()
            .asExpected()
            .pageWasRendered("/test/asset/page-one/index.html") {
                htmlHeadMatches("head link[rel=stylesheet]") {
                    link(href="http://orchid.test/TestAssetTheme/1e240/${TestAssetTheme.CSS}", rel="stylesheet", type="text/css") { }
                    link(href="http://orchid.test/${TestAssetPage.CSS}", rel="stylesheet", type="text/css") { }
                    link(href="https://copper-leaf.github.io/test-downloadable-assets/assets/css/style.css", rel="stylesheet", type="text/css") { }
                }
                htmlBodyMatches {
                    div("component component-pageContent component-order-0") {}
                    script(src="http://orchid.test/TestAssetTheme/1e240/${TestAssetTheme.JS}") { }
                    script(src="http://orchid.test/${TestAssetPage.JS}") { }
                    script(src="https://copper-leaf.github.io/test-downloadable-assets/assets/js/scripts.js") { }
                }
            }
            .pageWasNotRendered("/test-downloadable-assets/assets/css/style.css")
            .pageWasNotRendered("/test-downloadable-assets/assets/js/scripts.js")
    }

}
