package com.eden.orchid.wiki.menu

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.indexing.OrchidInternalIndex
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.wiki.model.WikiModel
import com.eden.orchid.wiki.model.WikiSection
import com.eden.orchid.wiki.pages.WikiPage
import javax.inject.Inject

class WikiPagesMenuItemType @Inject
constructor(context: OrchidContext, private val model: WikiModel) : OrchidMenuItem(context, "wiki", 100) {

    @Option
    var section: String? = null

    @Option @BooleanDefault(false)
    var topLevel: Boolean = false

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        var menuItems: MutableList<OrchidMenuItemImpl> = ArrayList()

        val sections = HashMap<String?, WikiSection>()

        val menuItemTitle: String
        val menuSection: String

        val wikiSection = model.getSection(section)

        if (wikiSection != null) {
            sections.put(section, wikiSection)
            menuItemTitle = OrchidUtils.camelcaseToTitleCase(section) + " Wiki"
            menuSection = section ?: ""
        } else {
            sections.putAll(model.sections)
            menuItemTitle = "Wiki"
            menuSection = "wiki"
        }

        val wikiPagesIndex = OrchidInternalIndex(menuSection)

        for (value in sections.values) {
            val sectionPages = ArrayList<OrchidPage>(value.wikiPages)
            for (page in sectionPages) {
                val ref = OrchidReference(page.reference)
                if (!menuSection.equals("wiki", ignoreCase = true)) {
                    ref.stripFromPath("wiki")
                }
                wikiPagesIndex.addToIndex(ref.path, page)
            }
        }

        menuItems.add(OrchidMenuItemImpl(context, menuItemTitle, wikiPagesIndex))

        for (item in menuItems) {
            item.setIndexComparator(menuItemComparator)
        }

        if (topLevel) {
            val innerItems = ArrayList<OrchidMenuItemImpl>()

            for (item in menuItems) {
                if (item.isHasChildren) {
                    innerItems.addAll(item.children)
                } else {
                    innerItems.add(item)
                }
            }

            menuItems = innerItems
        }

        return menuItems
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

