package com.eden.orchid.impl.themes.menus

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.api.theme.pages.OrchidPage
import java.util.ArrayList

@Description("Adds all pages from a generator to the menu.", name = "Generator Pages")
class GeneratorPagesMenuItem : OrchidMenuFactory("generatorPages") {

    @Option
    @Description("The text of the root menu item.")
    lateinit var title: String

    @Option
    @Description("The generator to show all items for.")
    lateinit var generator: String

    override fun getMenuItems(
        context: OrchidContext,
        page: OrchidPage
    ): List<MenuItem> {
        val menuItems = ArrayList<MenuItem>()
        if (!EdenUtils.isEmpty(title) && !EdenUtils.isEmpty(generator)) {
            val foundIndex = context.index.findIndex(generator)
            if (foundIndex != null) {
                menuItems.add(
                    MenuItem.Builder(context)
                        .fromIndex(foundIndex)
                        .title(title)
                        .build()
                )
            }
        }

        return menuItems
    }
}
