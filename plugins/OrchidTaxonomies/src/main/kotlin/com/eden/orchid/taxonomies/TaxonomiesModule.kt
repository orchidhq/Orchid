package com.eden.orchid.taxonomies

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.eden.orchid.api.theme.permalinks.PermalinkPathType
import com.eden.orchid.taxonomies.permalink.pathtype.ArchiveIndexPathType
import com.eden.orchid.taxonomies.permalink.pathtype.TaxonomyPathType
import com.eden.orchid.taxonomies.permalink.pathtype.TermPathType

class TaxonomiesModule : OrchidModule() {

    override fun configure() {
        addToSet(OrchidGenerator::class.java,
                TaxonomiesGenerator::class.java)

        addToSet(PluginResourceSource::class.java,
                TaxonomiesResourceSource::class.java)

        addToSet(PermalinkPathType::class.java,
                TaxonomyPathType::class.java,
                TermPathType::class.java,
                ArchiveIndexPathType::class.java)
    }
}

