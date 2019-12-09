package com.eden.orchid.netlifycms

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.server.OrchidController
import com.eden.orchid.netlifycms.api.NetlifyCmsApiController
import com.eden.orchid.netlifycms.api.NetlifyCmsManageController
import com.eden.orchid.utilities.addToSet

class NetlifyCmsModule : OrchidModule() {

    override fun configure() {
        withResources(20)

        addToSet<OrchidGenerator<*>, NetlifyCmsGenerator>()
        addToSet<OrchidController>(
                NetlifyCmsApiController::class,
                NetlifyCmsManageController::class)
    }
}

