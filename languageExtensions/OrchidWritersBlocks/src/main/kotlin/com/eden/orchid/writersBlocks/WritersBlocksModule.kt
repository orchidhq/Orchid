package com.eden.orchid.writersBlocks

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.eden.orchid.writersBlocks.tags.AlertTag

class WritersBlocksModule : OrchidModule() {

    override fun configure() {
        addToSet(PluginResourceSource::class.java,
                WritersBlocksResourceSource::class.java)

        addToSet(TemplateTag::class.java,
                AlertTag::class.java)
    }
}

