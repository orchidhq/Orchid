package com.eden.orchid.testhelpers

import com.eden.orchid.api.registration.IgnoreModule
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.render.OrchidRenderer

@IgnoreModule
class TestImplModule(private val serve: Boolean) : OrchidModule() {

    override fun configure() {
        if (!serve) {
            bind(OrchidRenderer::class.java).to(TestRenderer::class.java)
        }
    }

}
