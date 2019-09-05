package com.eden.orchid.sourcedoc

import com.eden.orchid.api.options.OrchidFlags
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.api.theme.permalinks.PermalinkPathType
import com.eden.orchid.sourcedoc.menu.SourceDocPageLinksMenuItemType
import com.eden.orchid.sourcedoc.menu.SourceDocPagesMenuItemType
import com.eden.orchid.sourcedoc.permalink.pathtype.ModulePathType
import com.eden.orchid.sourcedoc.permalink.pathtype.ModuleTypePathType
import com.eden.orchid.sourcedoc.permalink.pathtype.SourceDocPathType
import com.eden.orchid.utilities.addToSet

class SourceDocModule : OrchidModule() {
    override fun configure() {
        if(OrchidFlags.getInstance().getFlagValue<Boolean?>("experimentalSourceDoc") == true) {
            withResources(10)
            addToSet<OrchidMenuFactory>(
                SourceDocPageLinksMenuItemType::class,
                SourceDocPagesMenuItemType::class
            )
            addToSet<PermalinkPathType>(
                ModulePathType::class,
                ModuleTypePathType::class,
                SourceDocPathType::class
            )
        }
    }
}
