package com.eden.orchid.impl.themes.menus

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.api.theme.pages.OrchidPage

@Description(
    "The subtree of all pages starting at a page in your site, referenced from a Collection. If no page " +
            "query is given, will use the current page. The subtree is defined as the Index at the page's path.",
    name = "Page Sub-Tree"
)
class PageSubtreeMenuItem : OrchidMenuFactory("pageSubtree") {

    @Option
    @Description("The Id of an item to look up.")
    lateinit var itemId: String

    @Option
    @Description("The type of collection the item is expected to come from.")
    lateinit var collectionType: String

    @Option
    @Description("The specific Id of the given collection type where the item is expected to come from.")
    lateinit var collectionId: String

    override fun getMenuItems(
        context: OrchidContext,
        page: OrchidPage
    ): List<MenuItem> {
        val page: OrchidPage = context.findPageOrDefault(collectionType, collectionId, itemId, page)
        val index = context.index.findIndex(page.reference.path)

        return if (index != null) {
            if (submenuTitle.isBlank()) {
                submenuTitle = page.title
            }

            MenuItem.Builder(context)
                .fromIndex(index)
                .build()
                .children
        } else {
            emptyList()
        }
    }
}
