package com.eden.orchid.api.options.globals

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.TemplateGlobal
import com.eden.orchid.api.theme.Theme
import com.eden.orchid.api.theme.pages.OrchidPage

class ThemeGlobal : TemplateGlobal<Theme?> {
    override fun key() = "theme"
    override fun get(context: OrchidContext, page: OrchidPage?): Theme? = page?.theme
}
