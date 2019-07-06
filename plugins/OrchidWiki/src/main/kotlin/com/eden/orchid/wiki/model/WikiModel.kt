package com.eden.orchid.wiki.model

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.wiki.pages.WikiSectionsPage

class WikiModel(
    sectionsList: List<WikiSection>
) : OrchidGenerator.Model {

    var sectionsPage: WikiSectionsPage? = null

    var sections: MutableMap<String, WikiSection> = linkedMapOf(*(sectionsList.map { it.key to it }.toTypedArray()))

    override val allPages: List<OrchidPage>
        get() {
            val allPages = ArrayList<OrchidPage>()
            for (value in sections.values) {
                allPages.add(value.summaryPage)
                allPages.addAll(value.wikiPages)
                if (value.bookPage != null) {
                    allPages.add(value.bookPage!!)
                }
            }
            if (sectionsPage != null) {
                allPages.add(sectionsPage!!)
            }

            return allPages
        }

    fun getSection(sectionKey: String): WikiSection? {
        if (sectionKey.isNotBlank()) {
            return sections.getOrDefault(sectionKey, null)
        }

        return null
    }
}
