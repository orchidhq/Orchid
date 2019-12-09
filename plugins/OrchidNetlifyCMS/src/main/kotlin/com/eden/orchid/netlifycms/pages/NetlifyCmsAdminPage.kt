package com.eden.orchid.netlifycms.pages

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.assets.CssPage
import com.eden.orchid.api.theme.assets.JsPage
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.netlifycms.model.NetlifyCmsModel

@Description(value = "The page hosting the Netlify CMS.", name = "Netlify CMS Page")
class NetlifyCmsAdminPage(
        resource: OrchidResource,
        val model: NetlifyCmsModel
) : OrchidPage(resource, "contentManager", "Orchid Content Manager") {

    override fun loadAssets() {
        model.loadAssets(this)
    }

    // override to prevent theme scripts and styles from being added to this page
    override fun collectThemeScripts(scripts: MutableList<JsPage>) {

    }

    override fun collectThemeStyles(styles: MutableList<CssPage>) {

    }

}
