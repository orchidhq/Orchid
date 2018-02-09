package com.eden.orchid.plugindocs.controllers

import com.caseyjbrooks.clog.Clog
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
import javax.inject.Inject

@JvmSuppressWildcards
class AdminController @Inject
constructor(val context: OrchidContext, val adminLists: Set<AdminList>) : OrchidController(1000) {

    @Get(path = "/")
    fun index(@Suppress("UNUSED_PARAMETER") request: OrchidRequest): OrchidResponse {
        val view = OrchidView(context, this, "admin")

        return OrchidResponse(context).view(view)
    }

    @Get(path = "/lists/:name/:id")
    fun renderListItem(@Suppress("UNUSED_PARAMETER") request: OrchidRequest, name: String, id: String): OrchidResponse {
        var foundList: AdminList? = null
        for (list in adminLists) {
            if (list.key.equals(name, ignoreCase = true)) {
                foundList = list
                break
            }
        }

        if (foundList != null) {
            val listItem = foundList.getItem(id)
            if (listItem != null) {
                val data = HashMap<String, Any>()
                data.put("adminList", foundList)
                data.put("listItem", listItem)

                val view = OrchidView(context, this, "adminListItem", data)
                view.title = id
                view.breadcrumbs = arrayOf("lists", foundList.key)

                return OrchidResponse(context).view(view)
            }
        }

        return OrchidResponse(context).status(404).content("List item not found")
    }

    @Get(path = "/withParams/:paramKey", params = AdminParams::class)
    fun testWithParams(@Suppress("UNUSED_PARAMETER") request: OrchidRequest, params: AdminParams, paramKey: String): OrchidResponse {
        val data = HashMap<String, Any>()
        data.put("params", params)

        val view = OrchidView(context, this, "adminParams", data)
        view.title = paramKey
        view.breadcrumbs = arrayOf("paramKey")

        return OrchidResponse(context).view(view)
    }

    public fun hasOptions(o: Any): Boolean {
        return o is OptionsHolder
    }

    public fun getOptions(o: Any): String {
        val extractor = context.injector.getInstance(OptionsExtractor::class.java)
        val table = extractor.getDescriptionTable(o.javaClass)

        var htmlTable = table.print(HtmlTableFormatter())
        htmlTable = htmlTable.replace("<table>".toRegex(), "<table class=\"table\">")
        Clog.v(htmlTable)

        return htmlTable
    }

    class AdminParams : OptionsHolder {

        @Option
        lateinit var param1: String

        @Option
        var param2: Int = 0

        @Option
        lateinit var param3: String

        @Option
        lateinit var paramKey: String

    }
}
