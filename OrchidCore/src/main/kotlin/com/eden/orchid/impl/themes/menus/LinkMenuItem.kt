package com.eden.orchid.impl.themes.menus

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.api.theme.pages.OrchidExternalPage
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.OrchidUtils
import java.util.ArrayList

@Description("A link to any generic URL.", name = "Link")
class LinkMenuItem : OrchidMenuFactory("link") {

    @Option
    @Description("The title of this menu item")
    lateinit var title: String

    @Option
    @Description(
        "The URL of this menu item. A URL of `/` links to the root of your site. A URL starting with a '#'" +
                "will link to an anchor on the current page."
    )
    lateinit var url: String

    override fun getMenuItems(context: OrchidContext): List<MenuItem> {
        val menuItems = ArrayList<MenuItem>()

        url = url.trim()

        if (!EdenUtils.isEmpty(title) && !EdenUtils.isEmpty(url)) {
            var reference: OrchidReference? = null

            // if link is just a slash, it is a link to the homepage
            if (url == "/") {
                url = context.baseUrl
            }

            // if the URL is not external, apply this site's base URL to make it absolute
            else if (!OrchidUtils.isExternal(url)) {
                url = OrchidUtils.applyBaseUrl(context, url)
            }

            // if the link is an ID, it should be a link to the current page at that ID
            else if (url.startsWith("#")) {
                reference = OrchidReference(page.reference)
                reference.id = url.substring(1)
            }

            if (reference == null) {
                reference = OrchidReference.fromUrl(context, title, url)
            }
            if (reference != null) {
                menuItems.add(
                    MenuItem.Builder(context)
                        .page(OrchidExternalPage(reference))
                        .title(title)
                        .build()
                )
            }
        }

        return menuItems
    }
}
