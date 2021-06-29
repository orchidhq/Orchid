package com.eden.orchid.netlifycms.pages

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.render.RenderService
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.assets.AssetManagerDelegate
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.netlifycms.model.NetlifyCmsModel

@Description(value = "The page hosting the Netlify CMS.", name = "Netlify CMS Page")
class NetlifyCmsAdminPage(
    resource: OrchidResource,
    val model: NetlifyCmsModel
) : OrchidPage(resource, RenderService.RenderMode.RAW, "contentManager", "Orchid Content Manager") {

    override fun loadAssets(delegate: AssetManagerDelegate) {
        model.loadAssets(delegate)
    }

    // TODO: prevent theme scripts and styles from being added to this page
}
