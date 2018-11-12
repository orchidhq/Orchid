package com.eden.orchid.wiki.pages

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.wiki.model.WikiSection

@Description(value = "An offline PDF with your wiki contents.", name = "Wiki Book")
class WikiBookPage(
        resource: WikiBookResource,
        val section: WikiSection
)
    : OrchidPage(resource, "wikiBook", "${section.sectionTitle} Book") {

}
