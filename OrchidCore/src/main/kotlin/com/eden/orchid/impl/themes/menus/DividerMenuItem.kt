package com.eden.orchid.impl.themes.menus

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import java.util.ArrayList
import javax.inject.Inject

@Description("A divider between sections of the menu, optionally with a title.", name = "Divider")
class DividerMenuItem
@Inject
constructor(
        context: OrchidContext
) : OrchidMenuItem(context, "separator", 100) {

    @Option
    @Description("An optional title for this divider, to create a contextual section within the menu.")
    lateinit var title: String

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val menuItems = ArrayList<OrchidMenuItemImpl>()

        if (!EdenUtils.isEmpty(title)) {
            menuItems.add(OrchidMenuItemImpl(context, title))
        }
        else {
            menuItems.add(OrchidMenuItemImpl(context))
        }

        return menuItems
    }
}
