package com.eden.orchid.pages.menu

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import org.apache.commons.lang3.StringUtils
import javax.inject.Inject

@Description("Static pages, optionally by group.", name = "Static Pages")
class PagesMenuType
@Inject
constructor(
        context: OrchidContext
) : OrchidMenuFactory(context, "pages", 100) {

    @Option
    @Description("Include only pages in a specific page group, otherwise include all pages.")
    lateinit var group: String

    private var menuItemComparator = Comparator { o1: MenuItem, o2: MenuItem ->
        var o1Title = ""
        var o2Title = ""

        if (o1.page != null) {
            o1Title = o1.page!!.title
        }
        else if (o1.isHasChildren && o1.children.size > 0 && o1.children[0].page != null) {
            o1Title = o1.children[0].title
        }

        if (o2.page != null) {
            o2Title = o2.page!!.title
        }
        else if (o2.isHasChildren && o2.children.size > 0 && o2.children[0].page != null) {
            o2Title = o2.children[0].title
        }

        o1Title.compareTo(o2Title)
    }

    override fun getMenuItems(): List<MenuItem> {
        val menuItems = ArrayList<MenuItem>()

        val allPages = context.index.getChildIndex<OrchidGenerator.Model>("pages")!!.allPages

        val pages = if (EdenUtils.isEmpty(group))
            allPages
        else
            allPages.filter { page -> page.reference.path.startsWith(group) }

        if (EdenUtils.isEmpty(submenuTitle)) {
            if (!EdenUtils.isEmpty(group)) {
                submenuTitle = StringUtils.capitalize(group)
            }
            else {
                submenuTitle = "Pages"
            }
        }
        menuItems.addAll(pages.map {
            MenuItem.Builder(context)
                    .page(it)
                    .indexComparator(menuItemComparator)
                    .build()
        })

        menuItems.sortWith(menuItemComparator)

        return menuItems
    }
}

