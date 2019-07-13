package com.eden.orchid.netlifycms.api

import com.eden.orchid.api.server.OrchidController
import com.eden.orchid.api.server.OrchidRequest
import com.eden.orchid.api.server.OrchidResponse
import com.eden.orchid.api.server.annotations.Get
import com.eden.orchid.netlifycms.model.NetlifyCmsModel
import com.eden.orchid.netlifycms.pages.NetlifyCmsAdminView
import com.eden.orchid.utilities.SuppressedWarnings
import com.eden.orchid.utilities.resolve

class NetlifyCmsManageController : OrchidController(10000) {

    override fun getPathNamespace(): String {
        return "admin"
    }

    @Get(path = "/")
    fun index(@Suppress(SuppressedWarnings.UNUSED_PARAMETER) request: OrchidRequest): OrchidResponse {
        return manage(request)
    }

    @Get(path = "/manage")
    fun manage(@Suppress(SuppressedWarnings.UNUSED_PARAMETER) request: OrchidRequest): OrchidResponse {
        val model: NetlifyCmsModel = request.context.resolve()
        val view = NetlifyCmsAdminView(request.context, this, model)
        return OrchidResponse(request.context).view(view)
    }
}
