package com.eden.orchid.api.options.globals

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.indexing.OrchidIndex
import com.eden.orchid.api.options.TemplateGlobal

class IndexGlobal : TemplateGlobal<OrchidIndex?> {
    override fun key() = "index"
    override fun get(context: OrchidContext): OrchidIndex? = context.index
}
