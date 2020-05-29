package com.eden.orchid.impl.themes.assets

import com.eden.orchid.strikt.asExpected
import com.eden.orchid.strikt.htmlBodyMatches
import com.eden.orchid.strikt.htmlHeadMatches
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import kotlinx.html.div
import kotlinx.html.link
import kotlinx.html.script
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TestNormalAssetsAttached : OrchidIntegrationTest(
    TestAssetsModule()
) {

    @BeforeEach
    fun setUp() {
        TestAssetsModule.addNormalAssetsToTest(this)
        flag("logLevel", "FATAL")
    }

    @Test
    @DisplayName("Ensure assets from components, pages, and themes are added to the page")
    fun test01() {
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
            |       {"type": "${TestAssetComponent.KEY}"}
            |   ],
            |   "metaComponents": [
            |       {"type": "${TestAssetMetaComponent.KEY}"}
            |   ]
            |}
            """.trimMargin()
        )

        execute()
            .asExpected()
            .pageWasRendered("/test/asset/page-one/index.html") {
                htmlHeadMatches("head link[rel=stylesheet]") {
                    link(href="http://orchid.test/TestAssetTheme/1e240/${TestAssetTheme.CSS}", rel="stylesheet", type="text/css") { }
                    link(href="http://orchid.test/${TestAssetComponent.CSS}", rel="stylesheet", type="text/css") { }
                    link(href="http://orchid.test/${TestAssetMetaComponent.CSS}", rel="stylesheet", type="text/css") { }
                    link(href="http://orchid.test/${TestAssetPage.CSS}", rel="stylesheet", type="text/css") { }
                }
                htmlBodyMatches {
                    div("component component-testAssetComponent component-order-0") {}
                    script(src="http://orchid.test/TestAssetTheme/1e240/${TestAssetTheme.JS}") { }
                    script(src="http://orchid.test/${TestAssetComponent.JS}") { }
                    script(src="http://orchid.test/${TestAssetMetaComponent.JS}") { }
                    script(src="http://orchid.test/${TestAssetPage.JS}") { }
                }
            }
    }

    @Test
    @DisplayName("Ensure assets are deduplicated")
    fun test02() {
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
            |       {"type": "${TestAssetComponent.KEY}"},
            |       {"type": "${TestAssetComponent.KEY}"},
            |       {"type": "${TestAssetComponent.KEY}"}
            |   ],
            |   "metaComponents": [
            |       {"type": "${TestAssetMetaComponent.KEY}"},
            |       {"type": "${TestAssetMetaComponent.KEY}"},
            |       {"type": "${TestAssetMetaComponent.KEY}"}
            |   ]
            |}
            """.trimMargin()
        )

        execute()
            .asExpected()
            .pageWasRendered("/test/asset/page-one/index.html") {
                htmlHeadMatches("head link[rel=stylesheet]") {
                    link(href="http://orchid.test/TestAssetTheme/1e240/${TestAssetTheme.CSS}", rel="stylesheet", type="text/css") { }
                    link(href="http://orchid.test/${TestAssetComponent.CSS}", rel="stylesheet", type="text/css") { }
                    link(href="http://orchid.test/${TestAssetMetaComponent.CSS}", rel="stylesheet", type="text/css") { }
                    link(href="http://orchid.test/${TestAssetPage.CSS}", rel="stylesheet", type="text/css") { }
                }
                htmlBodyMatches {
                    div("component component-testAssetComponent component-order-0") {}
                    div("component component-testAssetComponent component-order-0") {}
                    div("component component-testAssetComponent component-order-0") {}
                    script(src="http://orchid.test/TestAssetTheme/1e240/${TestAssetTheme.JS}") { }
                    script(src="http://orchid.test/${TestAssetComponent.JS}") { }
                    script(src="http://orchid.test/${TestAssetMetaComponent.JS}") { }
                    script(src="http://orchid.test/${TestAssetPage.JS}") { }
                }
            }
    }
}
