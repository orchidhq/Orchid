package com.eden.orchid.netlifycms.api

import com.eden.orchid.api.server.OrchidController
import com.eden.orchid.api.server.OrchidRequest
import com.eden.orchid.api.server.OrchidResponse
import com.eden.orchid.api.server.OrchidView
import com.eden.orchid.api.server.annotations.AdminPage
import com.eden.orchid.api.server.annotations.Get
import com.eden.orchid.netlifycms.model.NetlifyCmsModel
import com.eden.orchid.utilities.SuppressedWarnings
import com.eden.orchid.utilities.resolve

class NetlifyCmsManageController : OrchidController(10000) {

    @Get(path = "/admin/manage")
    fun manage(@Suppress(SuppressedWarnings.UNUSED_PARAMETER) request: OrchidRequest): OrchidResponse {
        return manage(request)
    }

    @AdminPage("Netlify CMS", icon = "assets/svg/netlify.svg")
    @Get(path = "/admin/content")
    fun content(@Suppress(SuppressedWarnings.UNUSED_PARAMETER) request: OrchidRequest): OrchidResponse {
        val model: NetlifyCmsModel = request.context.resolve()
        val view = OrchidView(
            request.context,
            "Netlify CMS Content Manager",
            mapOf(
                "model" to model
            ),
            "manage"
        ).also {
            it.type = OrchidView.Type.Fullscreen
        }
        return OrchidResponse(request.context).view(view)
    }
}
