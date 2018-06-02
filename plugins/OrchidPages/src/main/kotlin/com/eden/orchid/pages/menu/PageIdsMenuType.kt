package com.eden.orchid.pages.menu

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import org.jsoup.Jsoup
import javax.inject.Inject

@Description(
        "Finds all headers with an ID within the page content and creates menu items for each."
)
class PageIdsMenuType @Inject
constructor(context: OrchidContext) : OrchidMenuItem(context, "pageIds", 100) {

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val menuItems = ArrayList<OrchidMenuItemImpl>()
        val doc = Jsoup.parse(page.content)
        val ids = doc.select("h1[id],h2[id],h3[id],h4[id],h5[id],h6[id]")
        for (id in ids) {
            val menuItem = OrchidMenuItemImpl(context, id.text())
            menuItem.isSeparator = false
            menuItem.anchor = id.attr("id")

            menuItems.add(menuItem)
        }

        return menuItems
    }
}

