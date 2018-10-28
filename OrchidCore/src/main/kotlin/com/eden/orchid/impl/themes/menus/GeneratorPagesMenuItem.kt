package com.eden.orchid.impl.themes.menus

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import java.util.ArrayList
import javax.inject.Inject

@Description("Adds all pages from a generator to the menu.", name = "Generator Pages")
class GeneratorPagesMenuItem
@Inject
constructor(
        context: OrchidContext
) : OrchidMenuItem(context, "generatorPages", 100) {

    @Option
    @Description("The text of the root menu item.")
    lateinit var title: String

    @Option
    @Description("The generator to show all items for.")
    lateinit var generator: String

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val menuItems = ArrayList<OrchidMenuItemImpl>()
        if (!EdenUtils.isEmpty(title) && !EdenUtils.isEmpty(generator)) {
            val foundIndex = context.internalIndex.findIndex(generator)
            if(foundIndex != null) {
                menuItems.add(
                        OrchidMenuItemImpl.Builder(context)
                                .fromIndex(foundIndex)
                                .title(title)
                                .build()
                )
            }
        }

        return menuItems
    }
}
