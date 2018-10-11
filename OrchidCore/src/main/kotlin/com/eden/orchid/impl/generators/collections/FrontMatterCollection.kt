package com.eden.orchid.impl.generators.collections

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.GlobalCollection
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.pages.OrchidPage

import javax.inject.Inject
import javax.inject.Singleton
import java.util.stream.Stream

@Singleton
@Description("A Front Matter Collection filters all pages in your site by a query against properties in a page's " + "Front Matter. A page is matched from a File Collection with an 'itemId' of the format 'key=value.")
class FrontMatterCollection @Inject
constructor(private val context: OrchidContext) : GlobalCollection<OrchidPage>("frontMatter") {

    override fun loadItems(): List<OrchidPage> {
        return context.internalIndex.allPages
    }

    override fun find(id: String): Stream<OrchidPage>? {
        // TODO: Make this able to evaluate simple queries including parentheses, & (and), and | (or) operators
        return if (id.contains("=")) {
            val key = id.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].trim { it <= ' ' }
            val value = id.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].trim { it <= ' ' }
            items.stream()
                .filter { page -> (if (page.get(key) != null) page.get(key).toString() else "") == value }
        } else {
            null
        }
    }

}
