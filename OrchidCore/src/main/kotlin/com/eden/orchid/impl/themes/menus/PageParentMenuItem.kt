package com.eden.orchid.impl.themes.menus

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import com.eden.orchid.api.theme.pages.OrchidPage
import java.util.ArrayList
import javax.inject.Inject

class PageParentMenuItem
@Inject
constructor(
        context: OrchidContext
) : OrchidMenuItem(context, "pageParent", 100) {

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

        val page: OrchidPage = (
                if (!EdenUtils.isEmpty(collectionType) || !EdenUtils.isEmpty(collectionId) || !EdenUtils.isEmpty(collectionId))
                    context.findPage(collectionType, collectionId, itemId)
                else
                    null
                ) ?: this.page

        if (page.parent != null) {
            val item = OrchidMenuItemImpl(context, page.parent)
            if (!EdenUtils.isEmpty(title)) {
                item.title = title
            }
            menuItems.add(item)
        }

        return menuItems
    }
}
