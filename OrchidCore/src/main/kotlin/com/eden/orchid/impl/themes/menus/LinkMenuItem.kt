package com.eden.orchid.impl.themes.menus

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import com.eden.orchid.api.theme.pages.OrchidExternalPage
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.OrchidUtils
import java.util.ArrayList
import javax.inject.Inject

@Description("A link to any generic URL.", name = "Link")
class LinkMenuItem
@Inject
constructor(
        context: OrchidContext
) : OrchidMenuItem(context, "link", 100) {

    @Option
    @Description("The title of this menu item")
    lateinit var title: String

    @Option
    @Description("The URL of this menu item. A URL of `/` links to the root of your site. A URL starting with a '#'" +
            "will link to an anchor on the current page."
    )
    lateinit var url: String

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val menuItems = ArrayList<OrchidMenuItemImpl>()

        url = url.trim()

        if (!EdenUtils.isEmpty(title) && !EdenUtils.isEmpty(url)) {
            var reference: OrchidReference? = null
            if (url == "/") {
                url = context.baseUrl
            }
            else if (!OrchidUtils.isExternal(url)) {
                url = OrchidUtils.applyBaseUrl(context, url)
            }
            else if (url.startsWith("#")) {
                reference = OrchidReference(page.reference)
                reference.id = url.substring(1)
            }

            if (reference == null) {
                reference = OrchidReference.fromUrl(context, title, url)
            }
            if (reference != null) {
                menuItems.add(OrchidMenuItemImpl(context, OrchidExternalPage(reference)))
            }
        }

        return menuItems
    }
}
