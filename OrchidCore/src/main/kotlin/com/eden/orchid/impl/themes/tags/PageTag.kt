package com.eden.orchid.impl.themes.tags

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description

@Description(value = "Render the main page components and page content.", name = "Page")
class PageTag : TemplateTag("page", Type.Simple, true) {

    override fun parameters() = emptyArray<String>()

}
