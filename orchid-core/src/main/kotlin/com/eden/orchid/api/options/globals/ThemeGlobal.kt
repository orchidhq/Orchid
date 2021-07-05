package com.eden.orchid.api.options.globals

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.TemplateGlobal
import com.eden.orchid.api.theme.Theme

class ThemeGlobal : TemplateGlobal<Theme?> {
    override fun key() = "theme"
    override fun get(context: OrchidContext): Theme? = context.theme
}
