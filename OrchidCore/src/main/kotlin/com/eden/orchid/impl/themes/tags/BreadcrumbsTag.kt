package com.eden.orchid.impl.themes.tags

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.impl.themes.breadcrumbs.Breadcrumb
import com.eden.orchid.impl.themes.breadcrumbs.BreadcrumbStrategy
import javax.inject.Inject

@Description(value = "Generate the page's breadcrumbs.", name = "Breadcrumbs")
class BreadcrumbsTag
@Inject
constructor(
        val strategy: BreadcrumbStrategy
) : TemplateTag("breadcrumbs", TemplateTag.Type.Simple, true) {

    override fun parameters(): Array<String> = emptyArray()

    val breadcrumbs: List<Breadcrumb> get() = strategy.getBreadcrumbs(page)

}
