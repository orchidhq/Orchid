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
import java.util.*
import javax.inject.Inject

class WikiPagesMenuItemType @Inject
constructor(context: OrchidContext, private val model: WikiModel) : OrchidMenuItem(context, "wiki", 100) {

    @Option
    var section: String? = null

    @Option @BooleanDefault(true)
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
            item.setIndexComparator(menuItemComparator);
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
        var o1Order = 0
        var o2Order = 0

        if (o1.page != null) {
            if (o1.page is WikiPage) {
                o1Order = (o1.page as WikiPage).order
            }
        }
        if (o2.page != null) {
            if (o2.page is WikiPage) {
                o2Order = (o2.page as WikiPage).order
            }
        }

        if (o1Order > 0 && o2Order > 0) {
            o1Order - o2Order
        } else {
            if (o1.isHasChildren) {
                if (o1.children.size > 0 && o1.children[0] != null && o1.children[0].page != null) {
                    if (o1.children[0].page is WikiPage) {
                        o1Order = (o1.children[0].page as WikiPage).order
                    }
                }
            }
            if (o2.isHasChildren) {
                if (o2.children.size > 0 && o2.children[0] != null && o2.children[0].page != null) {
                    if (o2.children[0].page is WikiPage) {
                        o2Order = (o2.children[0].page as WikiPage).order
                    }
                }
            }

            if (o1Order > 0 && o2Order > 0) {
                o1Order - o2Order
            }
        }

        0
    }


}

