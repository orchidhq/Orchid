package com.eden.orchid.api.options.globals

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.TemplateGlobal
import com.eden.orchid.api.site.OrchidSite

class SiteGlobal : TemplateGlobal<OrchidSite?> {
    override fun key() = "site"
    override fun get(context: OrchidContext): OrchidSite? = context.site
}
