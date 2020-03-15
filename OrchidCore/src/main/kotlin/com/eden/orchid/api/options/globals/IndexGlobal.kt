package com.eden.orchid.api.options.globals

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.indexing.OrchidIndex
import com.eden.orchid.api.options.TemplateGlobal
import com.eden.orchid.api.theme.pages.OrchidPage

class IndexGlobal : TemplateGlobal<OrchidIndex?> {
    override fun key() = "index"
    override fun get(context: OrchidContext, page: OrchidPage?): OrchidIndex? = context.index
}
