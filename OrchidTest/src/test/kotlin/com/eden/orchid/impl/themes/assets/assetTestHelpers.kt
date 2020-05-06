package com.eden.orchid.impl.themes.assets

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.generators.modelOf
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.render.RenderService
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.Theme
import com.eden.orchid.api.theme.assets.AssetManagerDelegate
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.utilities.addToSet
import javax.inject.Inject

class TestAssetTheme
@Inject
constructor(
    context: OrchidContext
) : Theme(context, KEY) {

    override fun loadAssets(delegate: AssetManagerDelegate) {
        delegate.addCss(CSS)
        delegate.addJs(JS)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false
        return true
    }

    override fun hashCode(): Int {
        return 123456
    }

    companion object {
        const val CSS = "assets/css/testThemeCss.css"
        const val EXTRA_CSS = "assets/css/extra/testThemeCss.css"
        const val JS = "assets/js/testThemeJs.js"
        const val EXTRA_JS = "assets/js/extra/testThemeJs.js"
        const val KEY = "TestAssetTheme"
        fun addAssetsToTest(test: OrchidIntegrationTest) {
            test.resource(CSS)
            test.resource(EXTRA_CSS)
            test.resource(JS)
            test.resource(EXTRA_JS)
        }
    }
}

class TestAssetPage
@Inject
constructor(
    resource: OrchidResource
) : OrchidPage(resource, RenderService.RenderMode.TEMPLATE, KEY, null) {

    override fun loadAssets(delegate: AssetManagerDelegate) {
        delegate.addCss(CSS)
        delegate.addJs(JS)
    }

    companion object {
        const val CSS = "assets/css/testPageCss.css"
        const val EXTRA_CSS = "assets/css/extra/testPageCss.css"
        const val JS = "assets/js/testPageJs.js"
        const val EXTRA_JS = "assets/js/extra/testPageJs.js"
        const val KEY = "testAssetPage"
        fun addAssetsToTest(test: OrchidIntegrationTest) {
            test.resource(CSS)
            test.resource(EXTRA_CSS)
            test.resource(JS)
            test.resource(EXTRA_JS)
        }
    }
}

class TestAssetGenerator : OrchidGenerator<OrchidGenerator.Model>(KEY, Stage.CONTENT) {
    override fun startIndexing(context: OrchidContext): Model {
        return modelOf {
            listOf(
                TestAssetPage(
                    StringResource(
                        OrchidReference(context, "test/asset/page-one.md"),
                        ""
                    )
                )
            )
        }
    }

    companion object {
        const val KEY = "testAssetGenerator"
    }
}

class TestAssetComponent : OrchidComponent(KEY) {
    override fun loadAssets(delegate: AssetManagerDelegate) {
        delegate.addCss(CSS)
        delegate.addJs(JS)
    }

    companion object {
        const val CSS = "assets/css/testComponentCss.css"
        const val EXTRA_CSS = "assets/css/extra/testComponentCss.css"
        const val JS = "assets/js/testComponentJs.js"
        const val EXTRA_JS = "assets/js/extra/testComponentJs.js"
        const val KEY = "testAssetComponent"
        fun addAssetsToTest(test: OrchidIntegrationTest) {
            test.resource(CSS)
            test.resource(EXTRA_CSS)
            test.resource(JS)
            test.resource(EXTRA_JS)
        }
    }
}

class TestAssetMetaComponent : OrchidComponent(KEY, true) {
    override fun loadAssets(delegate: AssetManagerDelegate) {
        delegate.addCss(CSS)
        delegate.addJs(JS)
    }

    companion object {
        const val CSS = "assets/css/testMetaComponentCss.css"
        const val EXTRA_CSS = "assets/css/extra/testMetaComponentCss.css"
        const val JS = "assets/js/testMetaComponentJs.js"
        const val EXTRA_JS = "assets/js/extra/testMetaComponentJs.js"
        const val KEY = "testAssetMetaComponent"
        fun addAssetsToTest(test: OrchidIntegrationTest) {
            test.resource(CSS)
            test.resource(EXTRA_CSS)
            test.resource(JS)
            test.resource(EXTRA_JS)
        }
    }
}

class TestAssetMetaComponentWithNoResources : OrchidComponent(KEY, true) {
    companion object {
        const val KEY = "testAssetMetaComponentWithNoResources"
    }
}

class TestAssetsModule : OrchidModule() {

    override fun configure() {
        super.configure()

        addToSet<OrchidComponent, TestAssetComponent>()
        addToSet<OrchidComponent, TestAssetMetaComponent>()
        addToSet<OrchidComponent, TestAssetMetaComponentWithNoResources>()
        addToSet<OrchidGenerator<*>, TestAssetGenerator>()
        addToSet<Theme, TestAssetTheme>()
    }

    companion object {
        fun addNormalAssetsToTest(test: OrchidIntegrationTest) {
            TestAssetPage.addAssetsToTest(test)
            TestAssetComponent.addAssetsToTest(test)
            TestAssetMetaComponent.addAssetsToTest(test)
            TestAssetTheme.addAssetsToTest(test)
        }
    }
}
