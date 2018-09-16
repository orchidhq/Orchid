package com.eden.orchid.impl.themes.menus

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import java.util.ArrayList
import javax.inject.Inject

class PageMenuItem
@Inject
constructor(
        context: OrchidContext
) : OrchidMenuItem(context, "page", 100) {

    @Option
    @Description("The title of this menu item")
    lateinit var title: String

    @Option
    @Description("The Id of an item to look up.")
    lateinit var itemId: String

    @Option
    @Description("The type of collection the item is expected to come from.")
    lateinit var collectionType: String

    @Option
    @Description("The specific Id of the given collection type where the item is expected to come from.")
    lateinit var collectionId: String

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val menuItems = ArrayList<OrchidMenuItemImpl>()

        val page = context.findPage(collectionType, collectionId, itemId)

        if (page != null) {
            val item = OrchidMenuItemImpl(context, page)
            if (!EdenUtils.isEmpty(title)) {
                item.title = title
            }
            menuItems.add(item)
        }

        return menuItems
    }
}
