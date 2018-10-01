package com.eden.orchid.plugindocs.controllers

import com.copperleaf.krow.KrowTable
import com.copperleaf.krow.formatters.html.DefaultHtmlAttributes
import com.copperleaf.krow.formatters.html.HtmlTableFormatter
import com.copperleaf.krow.krow
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.Descriptive
import com.eden.orchid.api.options.OptionHolderDescription
import com.eden.orchid.api.options.OptionsDescription
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.server.OrchidController
import com.eden.orchid.api.server.OrchidRequest
import com.eden.orchid.api.server.OrchidResponse
import com.eden.orchid.api.server.OrchidView
import com.eden.orchid.api.server.annotations.Get
import org.apache.commons.lang3.ClassUtils
import java.net.URLEncoder
import javax.inject.Inject

@JvmSuppressWildcards
class AdminController
@Inject
constructor(
        val context: OrchidContext
) : OrchidController(1000) {

    val tableAttrs = object : DefaultHtmlAttributes() {
        override val tableClass: String get() = "uk-table uk-table-divider"
        override val leaderClass: String get() = "uk-hidden"
    }

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
            data.putAll(request.all().toMap())

            val view = OrchidView(context, this, data, "describe")
            view.title = Descriptive.getDescriptiveName(params.classType!!)
            view.breadcrumbs = arrayOf("describe", params.classType!!.`package`.name)
            view.params = params

            return OrchidResponse(context).view(view)
        }

        return OrchidResponse(context).status(404).content("Class ${params.className} not found")
    }

    fun getOptions(o: Class<*>, includeOwnOptions: Boolean, includeInheritedOptions: Boolean): String {
        val extractor = context.injector.getInstance(OptionsExtractor::class.java)
        val description = extractor.describeOptions(o, includeOwnOptions, includeInheritedOptions)
        val table = getDescriptionTable(description)

        return table.print(HtmlTableFormatter(tableAttrs))
    }

    fun getClassTypeOverviewTemplate(o: Class<*>): String {
        val classes = ArrayList<String>()

        // first add the class and its superclasses, to make sure that the concrete types get precedence over interfaces
        classes.add("server/description/" + o.name.replace(".", "/"))
        ClassUtils.getAllSuperclasses(o).forEach {
            classes.add("server/description/" + it.name.replace(".", "/"))
        }

        // fall back to interface class templates
        ClassUtils.getAllInterfaces(o).forEach {
            classes.add("server/description/" + it.name.replace(".", "/"))
        }

        return "?" + classes.joinToString(",")
    }

    fun <T> provide(o: Class<T>): T? {
        try {
            return context.injector.getInstance(o)
        }
        catch (e: Exception) {

        }

        return null
    }

    fun getDescriptionTable(optionsHolderDescription: OptionHolderDescription): KrowTable {
        return krow {
            optionsHolderDescription.optionsDescriptions.forEach { option ->
                cell("Key", option.key) {
                    content = "<code>${option.key}</code>"
                }
                cell("Type", option.key) {
                    content = toAnchor(option)
                }
                cell("Default Value", option.key) {
                    content = option.defaultValue
                }
                cell("Description", option.key) {
                    content = option.description
                }
            }
        }
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

    private fun toAnchor(option: OptionsDescription): String {
        val baseLink = "<a href='${getDescriptionLink(option.optionType)}'>${option.optionType.simpleName}</a>"
        val typeParamLinks: String

        if(!EdenUtils.isEmpty(option.optionTypeParameters)) {
            typeParamLinks = option.optionTypeParameters
                    .map { "<a href='${getDescriptionLink(it)}'>${it.simpleName}</a>" }
                    .joinToString(separator = ", ", prefix = "&lt;", postfix = "&gt;")
        }
        else {
            typeParamLinks = ""
        }

        return baseLink + typeParamLinks
    }

}
