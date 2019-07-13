package com.eden.orchid.pages.menu

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import org.jsoup.Jsoup
import java.util.stream.IntStream
import kotlin.streams.toList


@Description(
    "Finds all headers with an ID within the page content and creates menu items for each. All headers " +
            "between the min and max levels are used, such as h1-h3, or h2-h5. The default is all headers, h1-h6. These " +
            "header links can either be displayed in a flat list in the order they were found on the page, or as a " +
            "nested tree, where h2s are grouped under their previous h1, etc.",
    name = "Page Ids"
)
class PageIdsMenuType : OrchidMenuFactory("pageIds") {

    @Option
    @IntDefault(1)
    @Description("The first 'level' of header to match. Defaults to h1.")
    var maxLevel: Int = 1

    @Option
    @IntDefault(6)
    @Description("The last 'level' of header to match. Defaults to h6.")
    var minLevel: Int = 6

    @Option
    @StringDefault("flat")
    @Description("The structure used to display the items. One of [flat, nested].")
    lateinit var structure: Structure

    override fun getMenuItems(context: OrchidContext): List<MenuItem> {
        if (maxLevel >= minLevel) {
            Clog.w("maxLevel must be less than minLevel")
            return emptyList()
        }

        val menuItems = ArrayList<MenuItem.Builder>()
        val headerLevelMap = HashMap<String, Int>()
        val doc = Jsoup.parse(page.content)

        val selector: String = IntStream
            .rangeClosed(maxLevel, minLevel)
            .mapToObj { i -> "h$i[id]" }
            .toList()
            .joinToString(separator = ",")

        val ids = doc.select(selector)
        for (id in ids) {
            val menuItem = MenuItem.Builder(context)
                .title(id.text())
                .anchor(id.attr("id"))

            headerLevelMap[id.attr("id")] = Integer.parseInt(id.tag().name.substring(1))

            menuItems.add(menuItem)
        }

        if (structure == Structure.nested) {
            val mostRecent = arrayOfNulls<MenuItem.Builder>(7)
            mostRecent[0] = MenuItem.Builder(context)

            for (menuItem in menuItems) {
                val level = headerLevelMap[menuItem.anchor]!!
                mostRecent[level] = menuItem

                var offset = 1
                var parent = mostRecent[level - offset]
                while (parent == null && offset < level) {
                    offset++
                    parent = mostRecent[level - offset]
                }
                if (parent != null) {
                    parent.child(menuItem)
                }
                mostRecent[level] = menuItem
            }

            return mostRecent[0]?.build()?.children ?: ArrayList()
        } else {
            return menuItems.map { it.build() }
        }
    }

    enum class Structure {
        flat, nested
    }
}

