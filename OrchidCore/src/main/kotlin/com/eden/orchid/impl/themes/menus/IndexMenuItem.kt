package com.eden.orchid.impl.themes.menus

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import java.util.ArrayList
import javax.inject.Inject

class IndexMenuItem
@Inject
constructor(
        context: OrchidContext
) : OrchidMenuItem(context, "index", 100) {

    @Option
    @Description("The text of the root menu item.")
    lateinit var title: String

    @Option
    @Description("Add all pages from the given generator under this menu item.")
    lateinit var index: String

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val menuItems = ArrayList<OrchidMenuItemImpl>()
        if (!EdenUtils.isEmpty(title) && !EdenUtils.isEmpty(index)) {
            val foundIndex = context.index.findIndex(index)
            menuItems.add(OrchidMenuItemImpl(context, title, foundIndex!!))
        }

        return menuItems
    }
}
