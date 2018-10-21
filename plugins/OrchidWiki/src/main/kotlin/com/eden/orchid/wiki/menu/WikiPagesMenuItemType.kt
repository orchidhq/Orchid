package com.eden.orchid.wiki.menu

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.indexing.OrchidIndex
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import com.eden.orchid.wiki.model.WikiModel
import com.eden.orchid.wiki.model.WikiSection
import com.eden.orchid.wiki.pages.WikiPage
import javax.inject.Inject

@Description("Links to all pages in your wiki, optionally by section.", name = "Wiki Pages")
class WikiPagesMenuItemType
@Inject
constructor(
        context: OrchidContext,
        private val model: WikiModel
) : OrchidMenuItem(context, "wiki", 100) {

    @Option
    @Description("The wiki section to include in this menu.")
    var section: String? = null

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val menuItems = ArrayList<OrchidMenuItemImpl>()

        val sections = HashMap<String?, WikiSection>()

        val menuItemTitle: String

        val wikiSection = model.getSection(section)

        if (wikiSection != null) {
            sections.put(section, wikiSection)
            menuItemTitle = wikiSection.title
        } else {
            sections.putAll(model.sections)
            menuItemTitle = "Wiki"
        }

        if(submenuTitle.isBlank()) {
            submenuTitle = menuItemTitle
        }

        val wikiPagesIndex = OrchidIndex(null, "wiki")

        for (value in sections.values) {
            for(page in value.wikiPages) {
                wikiPagesIndex.addToIndex(page.reference.path, page)
            }
        }

        menuItems.add(OrchidMenuItemImpl(context, "", wikiPagesIndex))

        for (item in menuItems) {
            item.setIndexComparator(menuItemComparator)
        }

        val innerItems = ArrayList<OrchidMenuItemImpl>()
        for (item in menuItems) {
            if (item.isHasChildren) {
                innerItems.addAll(item.children)
            } else {
                innerItems.add(item)
            }
        }

        return innerItems
    }

    private var menuItemComparator = { o1: OrchidMenuItemImpl, o2: OrchidMenuItemImpl ->
        var o1WikiPage = getWikiPageFromMenuItem(o1)
        var o2WikiPage = getWikiPageFromMenuItem(o2)

        var o1Order = if(o1WikiPage != null) o1WikiPage.order else 0
        var o2Order = if(o2WikiPage != null) o2WikiPage.order else 0

        o1Order.compareTo(o2Order)
    }

    private fun getWikiPageFromMenuItem(item: OrchidMenuItemImpl): WikiPage? {
        if(item.page != null && item.page is WikiPage) {
            return item.page as WikiPage
        }
        else if(item.children.isNotEmpty() && item.children.first() != null && item.children.first().page is WikiPage){
            return item.children.first().page as WikiPage
        }
        
        return null
    }


}

