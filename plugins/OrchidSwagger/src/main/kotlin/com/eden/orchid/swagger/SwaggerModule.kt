package com.eden.orchid.swagger

import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.swagger.components.SwaggerComponent

class SwaggerModule : OrchidModule() {

    override fun configure() {
        addToSet(OrchidComponent::class.java,
                SwaggerComponent::class.java)

        addToSet(PluginResourceSource::class.java,
                SwaggerResourceSource::class.java)
    }

}

