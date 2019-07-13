package com.eden.orchid.impl.themes.menus

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import java.util.ArrayList

@Description("A divider between sections of the menu, optionally with a title.", name = "Divider")
class DividerMenuItem : OrchidMenuFactory("separator") {

    @Option
    @Description("An optional title for this divider, to create a contextual section within the menu.")
    lateinit var title: String

    override fun getMenuItems(context: OrchidContext): List<MenuItem> {
        val menuItems = ArrayList<MenuItem>()

        if (!EdenUtils.isEmpty(title)) {
            menuItems.add(
                MenuItem.Builder(context)
                    .title(title)
                    .separator(true)
                    .build()
            )
        } else {
            menuItems.add(
                MenuItem.Builder(context)
                    .separator(true)
                    .build()
            )
        }

        return menuItems
    }
}
