package com.eden.orchid.impl.themes.tags

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description

import javax.inject.Inject

@Description(value = "Render the main page components and page content.", name = "Page")
class PageTag @Inject
constructor() : TemplateTag("page", TemplateTag.Type.Simple, true) {

    override fun parameters(): Array<String> {
        return arrayOf()
    }

}
