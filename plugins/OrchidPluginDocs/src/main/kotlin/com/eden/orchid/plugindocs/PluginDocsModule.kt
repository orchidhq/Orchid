package com.eden.orchid.plugindocs

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.eden.orchid.api.server.OrchidController
import com.eden.orchid.api.theme.AdminTheme
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.plugindocs.components.PluginDocsComponent
import com.eden.orchid.plugindocs.controllers.AdminController
import com.eden.orchid.plugindocs.tags.PluginDocsTag

class PluginDocsModule : OrchidModule() {

    override fun configure() {
        addToSet(PluginResourceSource::class.java,
                PluginDocsResourceSource::class.java)

        addToSet(OrchidComponent::class.java,
                PluginDocsComponent::class.java)

        addToSet(TemplateTag::class.java,
                PluginDocsTag::class.java)

        addToSet(OrchidController::class.java,
                AdminController::class.java)

        addToSet(AdminTheme::class.java,
                DefaultAdminTheme::class.java)
    }

}
