package com.eden.orchid.taxonomies

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.permalinks.PermalinkPathType
import com.eden.orchid.taxonomies.components.TaxonomyComponent
import com.eden.orchid.taxonomies.components.TaxonomyTermComponent
import com.eden.orchid.taxonomies.menus.TaxonomiesMenuType
import com.eden.orchid.taxonomies.menus.TaxonomyMenuType
import com.eden.orchid.taxonomies.menus.TaxonomyTermMenuType
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

        addToSet(OrchidComponent::class.java,
                TaxonomyComponent::class.java,
                TaxonomyTermComponent::class.java)

        addToSet(OrchidMenuItem::class.java,
                TaxonomiesMenuType::class.java,
                TaxonomyMenuType::class.java,
                TaxonomyTermMenuType::class.java)
    }
}

