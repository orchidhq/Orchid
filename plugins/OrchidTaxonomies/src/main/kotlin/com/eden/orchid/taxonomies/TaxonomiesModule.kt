package com.eden.orchid.taxonomies

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.api.theme.permalinks.PermalinkPathType
import com.eden.orchid.taxonomies.components.TaxonomyComponent
import com.eden.orchid.taxonomies.components.TaxonomyTermComponent
import com.eden.orchid.taxonomies.menus.TaxonomiesMenuType
import com.eden.orchid.taxonomies.menus.TaxonomyMenuType
import com.eden.orchid.taxonomies.menus.TaxonomyTermMenuType
import com.eden.orchid.taxonomies.permalink.pathtype.ArchiveIndexPathType
import com.eden.orchid.taxonomies.permalink.pathtype.TaxonomyPathType
import com.eden.orchid.taxonomies.permalink.pathtype.TermPathType
import com.eden.orchid.utilities.addToSet

class TaxonomiesModule : OrchidModule() {

    override fun configure() {
        withResources(20)

        addToSet<OrchidGenerator<*>, TaxonomiesGenerator>()
        addToSet<PermalinkPathType>(
                TaxonomyPathType::class,
                TermPathType::class,
                ArchiveIndexPathType::class)
        addToSet<OrchidComponent>(
                TaxonomyComponent::class,
                TaxonomyTermComponent::class)
        addToSet<OrchidMenuFactory>(
                TaxonomiesMenuType::class,
                TaxonomyMenuType::class,
                TaxonomyTermMenuType::class)
    }
}

