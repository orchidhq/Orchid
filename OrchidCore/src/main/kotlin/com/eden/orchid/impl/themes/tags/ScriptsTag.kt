package com.eden.orchid.impl.themes.tags

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description

@Description(value = "Render all script tags to the page.", name = "Scripts")
class ScriptsTag : TemplateTag("scripts", Type.Simple, true) {

    override fun parameters(): Array<String> {
        return arrayOf()
    }

}
