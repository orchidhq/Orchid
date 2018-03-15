package com.eden.orchid.netlifycms

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.eden.orchid.api.server.OrchidController
import com.eden.orchid.netlifycms.api.NetlifyCmsApiController
import com.eden.orchid.netlifycms.api.NetlifyCmsManageController

class NetlifyCmsModule : OrchidModule() {

    override fun configure() {
        addToSet(PluginResourceSource::class.java,
                NetlifyCmsResourceSource::class.java)

        addToSet(OrchidGenerator::class.java,
                NetlifyCmsGenerator::class.java)

        addToSet(OrchidController::class.java,
                NetlifyCmsApiController::class.java,
                NetlifyCmsManageController::class.java)
    }
}

