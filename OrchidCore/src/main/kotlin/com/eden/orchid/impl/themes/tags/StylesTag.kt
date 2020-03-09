package com.eden.orchid.impl.themes.tags

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description

@Description(value = "Render all style tags to the page.", name = "Styles")
class StylesTag : TemplateTag("styles", Type.Simple, true) {

    override fun parameters() = emptyArray<String>()

}
