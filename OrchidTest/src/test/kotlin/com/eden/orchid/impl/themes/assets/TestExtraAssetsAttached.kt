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

class TestExtraAssetsAttached : OrchidIntegrationTest(
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
            "theme",
            """
            |{
            |   "extraCss": [
            |       "${TestAssetTheme.EXTRA_CSS}"
            |   ],
            |   "extraJs": [
            |       "${TestAssetTheme.EXTRA_JS}"
            |   ]
            |}
            """.trimMargin()
        )
        configObject(
            "allPages",
            """
            |{
            |    "extraCss": [
            |       "${TestAssetPage.EXTRA_CSS}"
            |   ],
            |   "extraJs": [
            |       "${TestAssetPage.EXTRA_JS}"
            |   ],
            |   "components": [
            |       {
            |           "type": "${TestAssetComponent.KEY}",
            |           "extraCss": [
            |               "${TestAssetComponent.EXTRA_CSS}"
            |           ],
            |           "extraJs": [
            |               "${TestAssetComponent.EXTRA_JS}"
            |           ]
            |       }
            |   ],
            |   "metaComponents": [
            |       {
            |           "type": "${TestAssetMetaComponent.KEY}",
            |           "extraCss": [
            |               "${TestAssetMetaComponent.EXTRA_CSS}"
            |           ],
            |           "extraJs": [
            |               "${TestAssetMetaComponent.EXTRA_JS}"
            |           ]
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
                    link(href = "http://orchid.test/TestAssetTheme/1e240/${TestAssetTheme.EXTRA_CSS}", rel = "stylesheet", type = "text/css") { }
                    link(href = "http://orchid.test/${TestAssetComponent.CSS}", rel = "stylesheet", type = "text/css") { }
                    link(href = "http://orchid.test/${TestAssetComponent.EXTRA_CSS}", rel = "stylesheet", type = "text/css") { }
                    link(href = "http://orchid.test/${TestAssetMetaComponent.CSS}", rel = "stylesheet", type = "text/css") { }
                    link(href = "http://orchid.test/${TestAssetMetaComponent.EXTRA_CSS}", rel = "stylesheet", type = "text/css") { }
                    link(href = "http://orchid.test/${TestAssetPage.CSS}", rel = "stylesheet", type = "text/css") { }
                    link(href = "http://orchid.test/${TestAssetPage.EXTRA_CSS}", rel = "stylesheet", type = "text/css") { }
                }
                htmlBodyMatches {
                    div("component component-testAssetComponent component-order-0") {}
                    script(src = "http://orchid.test/TestAssetTheme/1e240/${TestAssetTheme.JS}") { }
                    script(src = "http://orchid.test/TestAssetTheme/1e240/${TestAssetTheme.EXTRA_JS}") { }
                    script(src = "http://orchid.test/${TestAssetComponent.JS}") { }
                    script(src = "http://orchid.test/${TestAssetComponent.EXTRA_JS}") { }
                    script(src = "http://orchid.test/${TestAssetMetaComponent.JS}") { }
                    script(src = "http://orchid.test/${TestAssetMetaComponent.EXTRA_JS}") { }
                    script(src = "http://orchid.test/${TestAssetPage.JS}") { }
                    script(src = "http://orchid.test/${TestAssetPage.EXTRA_JS}") { }
                }
            }
    }
}
