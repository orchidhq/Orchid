package com.eden.orchid.sourcedoc

import com.eden.orchid.api.options.OrchidFlags
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.api.theme.permalinks.PermalinkPathType
import com.eden.orchid.sourcedoc.components.SourcedocPageSimpleSummary
import com.eden.orchid.sourcedoc.menu.SourceDocModulesMenuItemType
import com.eden.orchid.sourcedoc.menu.SourceDocPageLinksMenuItemType
import com.eden.orchid.sourcedoc.menu.SourceDocPagesMenuItemType
import com.eden.orchid.sourcedoc.permalink.pathtype.ModuleGroupPathType
import com.eden.orchid.sourcedoc.permalink.pathtype.ModulePathType
import com.eden.orchid.sourcedoc.permalink.pathtype.ModuleTypePathType
import com.eden.orchid.sourcedoc.permalink.pathtype.SourceDocPathType
import com.eden.orchid.utilities.addToSet

class SourceDocModule : OrchidModule() {
    override fun configure() {
        if(OrchidFlags.getInstance().getFlagValue<Boolean?>("legacySourceDoc") != true) {
            withResources(10)
            addToSet<OrchidMenuFactory>(
                SourceDocPageLinksMenuItemType::class,
                SourceDocPagesMenuItemType::class,
                SourceDocModulesMenuItemType::class
            )
            addToSet<PermalinkPathType>(
                ModulePathType::class,
                ModuleTypePathType::class,
                ModuleGroupPathType::class,
                SourceDocPathType::class
            )
            addToSet<OrchidComponent>(
                SourcedocPageSimpleSummary::class
            )
        }
    }
}
