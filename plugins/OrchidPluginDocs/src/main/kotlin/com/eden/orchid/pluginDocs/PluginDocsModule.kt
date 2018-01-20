package com.eden.orchid.pluginDocs

import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.eden.orchid.api.server.OrchidController
import com.eden.orchid.api.server.admin.AdminList
import com.eden.orchid.api.theme.AdminTheme
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.pluginDocs.components.PluginDocsComponent
import com.eden.orchid.pluginDocs.controllers.AdminController
import com.eden.orchid.pluginDocs.lists.*

class PluginDocsModule : OrchidModule() {

    override fun configure() {
        addToSet(PluginResourceSource::class.java,
                PluginDocsResourceSource::class.java)

        addToSet(AdminList::class.java,
                CompilersList::class.java,
                ComponentsList::class.java,
                GeneratorsList::class.java,
                OptionsList::class.java,
                ParsersList::class.java,
                ResourceSourcesList::class.java,
                TasksList::class.java,
                ThemesList::class.java,
                MenuItemsList::class.java)

        addToSet(OrchidComponent::class.java,
                PluginDocsComponent::class.java)

        addToSet(OrchidController::class.java,
                AdminController::class.java)

        addToSet(AdminTheme::class.java,
                DefaultAdminTheme::class.java)
    }

}
