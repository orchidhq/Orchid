package com.eden.orchid.snippets.controllers

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.server.OrchidController
import com.eden.orchid.api.server.OrchidRequest
import com.eden.orchid.api.server.OrchidResponse
import com.eden.orchid.api.server.OrchidView
import com.eden.orchid.api.server.annotations.AdminPage
import com.eden.orchid.api.server.annotations.Get
import com.eden.orchid.snippets.models.SnippetsModel
import com.eden.orchid.utilities.SuppressedWarnings
import com.eden.orchid.utilities.resolve
import javax.inject.Inject

@JvmSuppressWildcards
class SnippetsController
@Inject
constructor(
    val context: OrchidContext
) : OrchidController(1000) {

    @AdminPage("Snippets", icon = "assets/svg/handcraft.svg")
    @Get(path = "/admin/snippets")
    fun index(@Suppress(SuppressedWarnings.UNUSED_PARAMETER) request: OrchidRequest): OrchidResponse {
        val model = context.resolve<SnippetsModel>()
        return OrchidResponse(context).view(
            OrchidView(
                context,
                "Snippet Manager",
                mapOf("snippetModel" to model),
                "snippets"
            )
        )
    }

}
