package com.eden.orchid.forms.controllers

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.server.OrchidController
import com.eden.orchid.api.server.OrchidRequest
import com.eden.orchid.api.server.OrchidResponse
import com.eden.orchid.api.server.OrchidView
import com.eden.orchid.api.server.annotations.AdminMenu
import com.eden.orchid.api.server.annotations.Get
import com.eden.orchid.forms.model.FormsModel
import com.eden.orchid.utilities.SuppressedWarnings
import com.eden.orchid.utilities.resolve
import javax.inject.Inject

@JvmSuppressWildcards
class FormsController
@Inject
constructor(
    val context: OrchidContext
) : OrchidController(1000) {

    @AdminMenu("Form Manager")
    @Get(path = "/admin/forms")
    fun index(@Suppress(SuppressedWarnings.UNUSED_PARAMETER) request: OrchidRequest): OrchidResponse {
        val model = context.resolve<FormsModel>()
        return OrchidResponse(context).view(
            OrchidView(
                context,
                this,
                "Form Manager",
                mapOf("formModel" to model),
                "forms"
            )
        )
    }

}
