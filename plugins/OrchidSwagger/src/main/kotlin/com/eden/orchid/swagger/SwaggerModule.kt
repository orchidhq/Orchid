package com.eden.orchid.swagger

import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.swagger.components.SwaggerComponent
import com.eden.orchid.utilities.addToSet

class SwaggerModule : OrchidModule() {

    override fun configure() {
        withResources()

        addToSet<OrchidComponent, SwaggerComponent>()
    }
}
