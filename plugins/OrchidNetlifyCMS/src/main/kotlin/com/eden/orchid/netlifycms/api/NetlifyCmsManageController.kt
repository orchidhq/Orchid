package com.eden.orchid.netlifycms.api

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.server.OrchidController
import com.eden.orchid.api.server.OrchidRequest
import com.eden.orchid.api.server.OrchidResponse
import com.eden.orchid.api.server.annotations.Get
import com.eden.orchid.netlifycms.model.NetlifyCmsModel
import com.eden.orchid.netlifycms.pages.NetlifyCmsAdminView
import com.eden.orchid.utilities.SuppressedWarnings
import javax.inject.Inject

class NetlifyCmsManageController
@Inject
constructor(
        val context: OrchidContext,
        val model: NetlifyCmsModel
) : OrchidController(10000) {

    override fun getPathNamespace(): String {
        return "admin"
    }

    @Get(path = "/")
    fun index(@Suppress(SuppressedWarnings.UNUSED_PARAMETER) request: OrchidRequest): OrchidResponse {
        return manage(request)
    }

    @Get(path = "/manage")
    fun manage(@Suppress(SuppressedWarnings.UNUSED_PARAMETER) request: OrchidRequest): OrchidResponse {
        val view = NetlifyCmsAdminView(context, this, model)
        return OrchidResponse(context).view(view)
    }
}
