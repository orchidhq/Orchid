package com.eden.orchid.pages.menu

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import org.jsoup.Jsoup
import java.util.stream.IntStream
import javax.inject.Inject
import kotlin.streams.toList


@Description("Finds all headers with an ID within the page content and creates menu items for each. All headers between the " +
        "min and max levels are used, such as h1-h3, or h2-h5. The default is all headers, h1-h6. These header links can " +
        "either be displayed in a flat list in the order they were found on the page, or as a nested tree, where h2s " +
        "are grouped under their previous h1, etc."
)
class PageIdsMenuType @Inject
constructor(context: OrchidContext) : OrchidMenuItem(context, "pageIds", 100) {

    @Option @IntDefault(1)
    @Description("The first 'level' of header to match. Defaults to h1.")
    var maxLevel: Int = 1

    @Option @IntDefault(6)
    @Description("The last 'level' of header to match. Defaults to h6.")
    var minLevel: Int = 6

    @Option @StringDefault("flat")
    @Description("The structure used to display the items. One of [flat, nested].")
    lateinit var structure: String

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val menuItems = ArrayList<OrchidMenuItemImpl>()
        val headerLevelMap = HashMap<String, Int>()
        val doc = Jsoup.parse(page.content)

        if(maxLevel >= minLevel) {
            Clog.w("maxLevel must be less than minLevel")
            return menuItems
        }

        val selector: String = IntStream
                .range(maxLevel, minLevel)
                .mapToObj({ i -> "h$i[id]" })
                .toList()
                .joinToString(separator=",")

        val ids = doc.select(selector)
        for (id in ids) {
            val menuItem = OrchidMenuItemImpl(context, id.text(), ArrayList())
            menuItem.isSeparator = false
            menuItem.anchor = id.attr("id")

            headerLevelMap[menuItem.anchor] = Integer.parseInt(id.tag().name.substring(1))

            menuItems.add(menuItem)
        }

        if(structure == "nested") {
            val mostRecent = arrayOfNulls<OrchidMenuItemImpl>(7)
            mostRecent[0] = OrchidMenuItemImpl(context, "", ArrayList())

            for(menuItem in menuItems) {
                val level = headerLevelMap[menuItem.anchor]!!
                mostRecent[level] = menuItem

                var offset = 1
                var parent = mostRecent[level - offset]
                while(parent == null && offset < level) {
                    offset++
                    parent = mostRecent[level - offset]
                }
                if(parent != null) {
                    if(parent.children == null) parent.children = ArrayList()
                    parent.children.add(menuItem)
                }
                mostRecent[level] = menuItem
            }

            return mostRecent[0]?.children?.filterNotNull()?.toList() ?: ArrayList()
        }
        else {
            return menuItems
        }
    }
}

