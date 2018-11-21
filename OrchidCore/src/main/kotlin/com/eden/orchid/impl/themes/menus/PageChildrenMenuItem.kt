package com.eden.orchid.impl.themes.menus

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.api.theme.pages.OrchidPage
import java.util.ArrayList
import javax.inject.Inject

@Description("The children of a page in your site, referenced from a Collection. If no page query is given, will use " +
        "the current page. The children are defined as the children of the Index at the page path.",
        name = "Page Children"
)
class PageChildrenMenuItem
@Inject
constructor(
        context: OrchidContext
) : OrchidMenuFactory(context, "pageChildren", 100) {

    @Option
    @Description("The Id of an item to look up.")
    lateinit var itemId: String

    @Option
    @Description("The type of collection the item is expected to come from.")
    lateinit var collectionType: String

    @Option
    @Description("The specific Id of the given collection type where the item is expected to come from.")
    lateinit var collectionId: String

    override fun getMenuItems(): List<MenuItem> {
        val menuItems = ArrayList<MenuItem>()

        val page: OrchidPage = (
                if (!EdenUtils.isEmpty(collectionType) || !EdenUtils.isEmpty(collectionId) || !EdenUtils.isEmpty(itemId))
                    context.findPage(collectionType, collectionId, itemId)
                else
                    null
                ) ?: this.page

        val pagePath = page.reference.path

        val index = context.internalIndex.findIndex(pagePath)

        if (index != null) {
            if (submenuTitle.isBlank()) {
                submenuTitle = page.title
            }

            menuItems.addAll(index
                    .children
                    .values
                    .flatMap { it.getOwnPages() }
                    .map {
                        MenuItem.Builder(context)
                                .page(it)
                                .build()
                    }
            )
        }

        return menuItems
    }
}
