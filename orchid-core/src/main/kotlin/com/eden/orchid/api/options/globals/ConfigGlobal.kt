package com.eden.orchid.api.options.globals

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.TemplateGlobal

class ConfigGlobal : TemplateGlobal<Map<String?, Any?>?> {
    override fun key() = "config"
    override fun get(context: OrchidContext): Map<String?, Any?>? = context.config
}
