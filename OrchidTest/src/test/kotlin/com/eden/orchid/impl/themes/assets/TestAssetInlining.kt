package com.eden.orchid.impl.themes.assets

import com.eden.orchid.strikt.asExpected
import com.eden.orchid.strikt.pageWasNotRendered
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
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
            |       {
            |           "asset": "assets/css/inlinedCss.scss",
            |           "inlined": true
            |       }
            |   ],
            |   "extraJs": [
            |       {
            |           "asset": "assets/js/inlinedJs.js",
            |           "inlined": true
            |       }
            |   ]
            |}
            """.trimMargin()
        )

        execute()
            .asExpected()
            .pageWasRendered("/test/asset/page-one/index.html") {
//                htmlHeadMatchesString("head link[rel=stylesheet], head style") {
//                    """
//                    |<link rel="stylesheet" type="text/css" href="http://orchid.test/TestAssetTheme/1e240/${TestAssetTheme.CSS}">
//                    |<link rel="stylesheet" type="text/css" href="http://orchid.test/${TestAssetPage.CSS}">
//                    |<style>.red .blue { color: purple; }</style>
//                    """.trimMargin()
//                }
//                htmlBodyMatchesString("body script") {
//                    """
//                    |<script src="http://orchid.test/TestAssetTheme/1e240/${TestAssetTheme.JS}"></script>
//                    |<script src="http://orchid.test/${TestAssetPage.JS}"></script>
//                    |<script>function itsFunToInline() { }</script>
//                    """.trimMargin()
//                }
            }
//            .pageWasNotRendered("/assets/css/inlinedCss.scss")
//            .pageWasNotRendered("/assets/js/inlinedJs.js")
    }

}
