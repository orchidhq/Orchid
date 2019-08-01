package com.eden.orchid.impl.themes.menus

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.api.theme.pages.OrchidPage

@Description(
    "A page in your site, referenced from a Collection. If no page query is given, will use the current page.",
    name = "Page"
)
class PageMenuItem : OrchidMenuFactory("page") {

    @Option
    @Description("The title of this menu item.")
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

    @Option
    @Description("The ID of an HTML element in the page to link to as an anchor.")
    lateinit var anchor: String

    override fun getMenuItems(
        context: OrchidContext,
        page: OrchidPage
    ): List<MenuItem> {
        val loadedPage: OrchidPage? = if (collectionType.isEmpty() && collectionId.isEmpty() && itemId.isEmpty()) {
            context.findPageOrDefault(collectionType, collectionId, itemId, page)
        } else {
            context.findPageOrDefault(collectionType, collectionId, itemId, null)
        }

        return if (loadedPage != null) {
            val item = MenuItem.Builder(context)

            if (anchor.isNotBlank()) {
                item.anchor = anchor

                // if this page is the menu item, just render the anchor, not the full link
                if (loadedPage !== page) {
                    item.page(loadedPage)
                }
            } else {
                item.page(loadedPage)
            }

            if (!EdenUtils.isEmpty(title)) {
                item.title(title)
            }

            listOf(item.build())
        } else {
            emptyList()
        }
    }
}
