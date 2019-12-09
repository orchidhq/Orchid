package com.eden.orchid.api.options.globals

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.TemplateGlobal

class DataGlobal : TemplateGlobal<Map<String?, Any?>?> {
    override fun key() = "data"
    override fun get(context: OrchidContext): Map<String?, Any?>? = context.data
}
