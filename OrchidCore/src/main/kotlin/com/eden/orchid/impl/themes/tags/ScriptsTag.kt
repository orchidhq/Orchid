package com.eden.orchid.impl.themes.tags

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description

import javax.inject.Inject

@Description(value = "Render all script tags to the page.", name = "Scripts")
class ScriptsTag @Inject
constructor() : TemplateTag("scripts", TemplateTag.Type.Simple, true) {

    override fun parameters(): Array<String> {
        return arrayOf()
    }

}