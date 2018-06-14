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
import com.eden.orchid.utilities.addToSet

class PluginDocsModule : OrchidModule() {

    override fun configure() {
        addToSet<PluginResourceSource, PluginDocsResourceSource>()
        addToSet<OrchidComponent, PluginDocsComponent>()
        addToSet<TemplateTag, PluginDocsTag>()
        addToSet<OrchidController, AdminController>()
        addToSet<AdminTheme, DefaultAdminTheme>()
    }

}
