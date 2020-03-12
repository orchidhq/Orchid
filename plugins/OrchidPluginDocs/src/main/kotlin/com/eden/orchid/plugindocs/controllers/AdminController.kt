package com.eden.orchid.plugindocs.controllers

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.Descriptive
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.server.OrchidController
import com.eden.orchid.api.server.OrchidRequest
import com.eden.orchid.api.server.OrchidResponse
import com.eden.orchid.api.server.OrchidView
import com.eden.orchid.api.server.annotations.AdminPage
import com.eden.orchid.api.server.annotations.Get
import com.eden.orchid.utilities.SuppressedWarnings
import java.net.URLEncoder
import javax.inject.Inject

@JvmSuppressWildcards
class AdminController
@Inject
constructor(
        val context: OrchidContext
) : OrchidController(1000) {

    @Get(path = "/admin")
    fun index(@Suppress(SuppressedWarnings.UNUSED_PARAMETER) request: OrchidRequest): OrchidResponse {
        val view = OrchidView(context, this, "admin")

        return OrchidResponse(context).view(view)
    }

    class DescribeParams : OptionsHolder {
        @Option lateinit var className: String
    }
    @Get(path = "/admin/describe", params = DescribeParams::class)
    fun describeClass(@Suppress(SuppressedWarnings.UNUSED_PARAMETER) request: OrchidRequest, params: DescribeParams): OrchidResponse {
        if (params.className.isNotBlank()) {
            val data = HashMap<String, Any>()
            data.putAll(request.all().toMap())

            try {
                val classType = Class.forName(params.className)
                val view = OrchidView(context, this, "${params.className} Description", data, "describe")
                view.title = Descriptive.getDescriptiveName(classType)
                view.breadcrumbs = arrayOf("describe", classType.`package`.name)
                view.params = params
                return OrchidResponse(context).view(view)
            }
            catch (e: Exception) {

            }
        }

        return OrchidResponse(context).status(404).content("Class ${params.className} not found")
    }

    private fun getDescriptionLink(o: Any): String {
        val className = o as? Class<*> ?: o.javaClass

        try {
            return context.baseUrl + "/admin/describe?className=" + URLEncoder.encode(className.name, "UTF-8")
        }
        catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

}
