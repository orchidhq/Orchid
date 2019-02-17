package com.eden.orchid.copper

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.generators.GlobalCollection
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.Theme
import com.eden.orchid.utilities.addToSet

class CopperModule : OrchidModule() {
    override fun configure() {
        addToSet<Theme, CopperTheme>()
        addToSet<TemplateTag, CopperTilesTag>()
        addToSet(GlobalCollection::class.java, CopperGithubProjectGlobalCollection::class.java)
    }
}
