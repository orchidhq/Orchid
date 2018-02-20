package com.eden.orchid.plugindocs.controllers

import com.eden.krow.formatters.HtmlTableFormatter
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.server.OrchidController
import com.eden.orchid.api.server.OrchidRequest
import com.eden.orchid.api.server.OrchidResponse
import com.eden.orchid.api.server.OrchidView
import com.eden.orchid.api.server.admin.AdminList
import com.eden.orchid.api.server.annotations.Get
import org.apache.commons.lang3.ClassUtils
import javax.inject.Inject

@JvmSuppressWildcards
class AdminController @Inject
constructor(val context: OrchidContext, val adminLists: Set<AdminList>) : OrchidController(1000) {

    @Get(path = "/")
    fun index(@Suppress("UNUSED_PARAMETER") request: OrchidRequest): OrchidResponse {
        val view = OrchidView(context, this, "admin")

        return OrchidResponse(context).view(view)
    }

    class DescribeParams : OptionsHolder {
        @Option lateinit var className: String

        val classType: Class<*>?
            get() {
                try {
                    return Class.forName(className)
                }
                catch (e: Exception) {
                    return null
                }
            }
    }
    @Get(path = "/describe", params = DescribeParams::class)
    fun describeClass(@Suppress("UNUSED_PARAMETER") request: OrchidRequest, params: DescribeParams): OrchidResponse {
        if (params.classType != null) {
            val data = HashMap<String, Any>()
            data["classType"] = params.classType!!

            val view = OrchidView(context, this, data, "describe")
            view.title = params.classType!!.simpleName
            view.breadcrumbs = arrayOf("describe", params.classType!!.`package`.name)

            return OrchidResponse(context).view(view)
        }

        return OrchidResponse(context).status(404).content("Class ${params.className} not found")
    }

    public fun getOptions(o: Class<*>, includeOwnOptions: Boolean, includeInheritedOptions: Boolean): String {
        val extractor = context.injector.getInstance(OptionsExtractor::class.java)
        val description = extractor.describeOptions(o, includeOwnOptions, includeInheritedOptions)
        val table = extractor.getDescriptionTable(description)

        var htmlTable = table.print(HtmlTableFormatter())
        htmlTable = htmlTable.replace("<table>".toRegex(), "<table class=\"table\">")

        return htmlTable
    }

    public fun getClassTypeOverviewTemplate(o: Class<*>): String {
        val classes = ArrayList<String>()

        // first add the class and its superclasses, to make sure that the concrete types get precedence over interfaces
        classes.add("server/admin/description/" + o.name.replace(".", "/"))
        ClassUtils.getAllSuperclasses(o).forEach {
            classes.add("server/admin/description/" + it.name.replace(".", "/"))
        }

        // fall back to interface class templates
        ClassUtils.getAllInterfaces(o).forEach {
            classes.add("server/admin/description/" + it.name.replace(".", "/"))
        }

        return "?" + classes.joinToString(",")
    }

    public fun <T> provide(o: Class<T>): T? {
        try {
            return context.injector.getInstance(o)
        }
        catch (e: Exception) {

        }

        return null
    }
}
