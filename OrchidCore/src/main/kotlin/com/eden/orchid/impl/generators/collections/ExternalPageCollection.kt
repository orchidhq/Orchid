package com.eden.orchid.impl.generators.collections

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.GlobalCollection
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.pages.OrchidPage

import javax.inject.Inject
import javax.inject.Singleton
import java.util.stream.Stream

@Singleton
@Description(
    "A External Page Collection represents all the pages included in the external indices of your site. " +
            "Pages are included into the external index by finding the index JSON file from another Orchid site, and " +
            "adding its link to your site's `services.generators.externalIndices` config.yml property. A page is matched " +
            "from an External Page Collection with an 'itemId' matching the page Title. Alternatively, you may query by " +
            "page path, using either slashes or dots."
)
class ExternalPageCollection @Inject
constructor(private val context: OrchidContext) : GlobalCollection<OrchidPage>("external") {

    override fun loadItems(): List<OrchidPage> {
        return context.externalIndex.allPages
    }

    override fun find(id: String): Stream<OrchidPage>? {
        if (id.contains("/") || id.contains(".")) {
            val page = context.externalIndex.findPage(id.replace("\\.", "/"))
            if (page != null) {
                return Stream.of(page)
            }
        } else {
            return items.stream().filter { page -> page.title == id || page.key == id }
        }

        return null
    }

}
