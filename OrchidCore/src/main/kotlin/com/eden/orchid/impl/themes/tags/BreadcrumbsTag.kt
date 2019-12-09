package com.eden.orchid.impl.themes.tags

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.impl.themes.breadcrumbs.Breadcrumb
import com.eden.orchid.impl.themes.breadcrumbs.BreadcrumbStrategy
import com.eden.orchid.utilities.resolve

@Description(value = "Generate the page's breadcrumbs.", name = "Breadcrumbs")
class BreadcrumbsTag : TemplateTag("breadcrumbs", Type.Simple, true) {

    override fun parameters(): Array<String> = emptyArray()

    val breadcrumbs: List<Breadcrumb> get() = context.resolve<BreadcrumbStrategy>().getBreadcrumbs(page)

}
