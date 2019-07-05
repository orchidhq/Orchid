package com.eden.orchid.impl.themes.menus

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import javax.inject.Inject

@Description("The siblings of a page in your site, referenced from a Collection. If no page query is given, will use " +
        "the current page. The siblings of a page are defined as the children of the parent Index, not necessarily " +
        "the Index at the parent Page.",
        name = "Page Siblings"
)
class PageSiblingsMenuItem
@Inject
constructor(
        context: OrchidContext
) : OrchidMenuFactory(context, "pageSiblings", 100) {

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

    override fun getMenuItems(): List<MenuItem> {
        if (submenuTitle.isBlank()) {
            submenuTitle = page.title
        }

        return context.index.findSiblingPages(collectionType, collectionId, itemId, page)
                .map {
                    MenuItem.Builder(context)
                            .page(it)
                            .build()
                }
    }
}
