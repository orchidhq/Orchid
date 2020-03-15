package com.eden.orchid.api.options.globals

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.TemplateGlobal
import com.eden.orchid.api.site.OrchidSite
import com.eden.orchid.api.theme.pages.OrchidPage

class SiteGlobal : TemplateGlobal<OrchidSite?> {
    override fun key() = "site"
    override fun get(context: OrchidContext, page: OrchidPage?): OrchidSite? = context.site
}
