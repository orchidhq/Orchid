package com.eden.orchid.pages.menu

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import javax.inject.Inject

class PageMenuType @Inject
constructor(context: OrchidContext) : OrchidMenuItem(context, "page", 100) {

    @Option
    @Description("The title of a Static Page to include in the menu.")
    lateinit var pageTitle: String

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val menuItems = ArrayList<OrchidMenuItemImpl>()

        val pages = context.internalIndex.getGeneratorPages("pages")

        if (!EdenUtils.isEmpty(pageTitle)) {
            for (page in pages) {
                if (page.title == pageTitle) {
                    menuItems.add(OrchidMenuItemImpl(context, page))
                }
            }
        }

        return menuItems
    }
}

