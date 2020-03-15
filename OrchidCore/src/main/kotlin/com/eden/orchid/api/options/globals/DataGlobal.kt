package com.eden.orchid.api.options.globals

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.TemplateGlobal
import com.eden.orchid.api.theme.pages.OrchidPage

class DataGlobal : TemplateGlobal<Map<String?, Any?>?> {
    override fun key() = "data"
    override fun get(context: OrchidContext, page: OrchidPage?): Map<String?, Any?>? = context.data
}
