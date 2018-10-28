package com.eden.orchid.impl.themes.menus

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.OrchidMenu
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import java.util.ArrayList
import javax.inject.Inject

@Description("A generic inner menu, which holds a menu inside it.", name = "Submenu")
class SubmenuMenuItem
@Inject
constructor(
        context: OrchidContext
) : OrchidMenuItem(context, "submenu", 100) {

    @Option
    @Description("The text of the root menu item.")
    lateinit var title: String

    @Option
    @Description("A new menu to nest under this menu item.")
    lateinit var menu: OrchidMenu

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val menuItems = ArrayList<OrchidMenuItemImpl>()

        if (!EdenUtils.isEmpty(title) && !menu.isEmpty) {
            menuItems.add(
                    OrchidMenuItemImpl.Builder(context)
                            .children(menu.getMenuItems(this.page))
                            .title(title)
                            .build()
            )
        }

        return menuItems
    }
}
