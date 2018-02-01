package com.eden.orchid.pages.menu

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import org.apache.commons.lang3.StringUtils
import java.util.TreeSet
import java.util.stream.Collectors
import javax.inject.Inject

class PagesMenuType @Inject
constructor(context: OrchidContext) : OrchidMenuItem(context, "pages", 100) {

    @Option
    var atRoot: Boolean = false

    @Option
    lateinit var section: String

    @Option
    lateinit var title: String

    internal var menuItemComparator = { o1: OrchidMenuItemImpl, o2: OrchidMenuItemImpl ->
        var o1Title = ""
        var o2Title = ""

        if (o1.page != null) {
            o1Title = o1.page.title
        } else if (o1.isHasChildren && o1.children.size > 0 && o1.children[0] != null && o1.children[0].page != null) {
            o1Title = o1.children[0].title
        }

        if (o2.page != null) {
            o2Title = o2.page.title
        } else if (o2.isHasChildren && o2.children.size > 0 && o2.children[0] != null && o2.children[0].page != null) {
            o2Title = o2.children[0].title
        }
        o1Title.compareTo(o2Title)
    }

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val menuItems = ArrayList<OrchidMenuItemImpl>()

        val allPages = context.internalIndex.getGeneratorPages("pages")

        val pages = if (EdenUtils.isEmpty(section))
            allPages
        else
            allPages
                    .stream()
                    .filter { page -> page.reference.path.startsWith(section) }
                    .collect(Collectors.toList())

        if (atRoot) {
            for (page in pages) {
                menuItems.add(OrchidMenuItemImpl(context, page))
            }
        } else {
            if (EdenUtils.isEmpty(title)) {
                if (!EdenUtils.isEmpty(section)) {
                    title = StringUtils.capitalize(section)
                } else {
                    title = "Pages"
                }
            }
            menuItems.add(OrchidMenuItemImpl(context, title, pages))
        }

        for (item in menuItems) {
            item.setIndexComparator(menuItemComparator)
        }

        return menuItems
    }
}

