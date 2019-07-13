package com.eden.orchid.impl.themes.menus

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.api.theme.pages.OrchidPage

@Description(
    "The parent of a page in your site, referenced from a Collection. If no page query is given, will use the " +
            "current page. The parent page is the same as the next level up in the breadcrumb hierarchy.",
    name = "Page Parent"
)
class PageParentMenuItem : OrchidMenuFactory("pageParent") {

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

    override fun getMenuItems(context: OrchidContext): List<MenuItem> {
        val page: OrchidPage = context.findPageOrDefault(collectionType, collectionId, itemId, page)

        return if (page.parent != null) {
            val item = MenuItem.Builder(context).page(page.parent)
            if (!EdenUtils.isEmpty(title)) {
                item.title(title)
            }
            listOf(item.build())
        } else {
            emptyList()
        }
    }
}
