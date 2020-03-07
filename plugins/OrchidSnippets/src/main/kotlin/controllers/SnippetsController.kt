package com.eden.orchid.snippets.controllers

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.Descriptive
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.server.OrchidController
import com.eden.orchid.api.server.OrchidRequest
import com.eden.orchid.api.server.OrchidResponse
import com.eden.orchid.api.server.OrchidView
import com.eden.orchid.api.server.annotations.Get
import com.eden.orchid.snippets.models.SnippetsModel
import com.eden.orchid.utilities.SuppressedWarnings
import com.eden.orchid.utilities.resolve
import java.net.URLEncoder
import javax.inject.Inject

@JvmSuppressWildcards
class SnippetsController
@Inject
constructor(
    val context: OrchidContext
) : OrchidController(1000) {

    override fun getPathNamespace(): String {
        return "/admin"
    }

    @Get(path = "/snippets")
    fun index(@Suppress(SuppressedWarnings.UNUSED_PARAMETER) request: OrchidRequest): OrchidResponse {
        val model = context.resolve<SnippetsModel>()
        return OrchidResponse(context).view(
            OrchidView(
                context,
                this,
                mapOf("snippetModel" to model),
                "snippets"
            )
        )
    }

}
