package com.eden.orchid.impl.themes.tags

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description

@Description(value = "All the default SEO tags that need to be in the HTML `<head>`.", name = "Head")
class HeadTag : TemplateTag("head", Type.Simple, true) {

    override fun parameters(): Array<String> {
        return arrayOf()
    }

}
