package com.eden.orchid.impl.themes.tags

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description

import javax.inject.Inject

@Description(value = "Generate the page's breadcrumbs.", name = "Breadcrumbs")
class BreadcrumbsTag @Inject
constructor() : TemplateTag("breadcrumbs", TemplateTag.Type.Simple, true) {

    var key: String? = null

    override fun parameters(): Array<String> {
        return arrayOf("key")
    }

}