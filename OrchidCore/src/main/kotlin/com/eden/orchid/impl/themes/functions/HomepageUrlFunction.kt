package com.eden.orchid.impl.themes.functions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.pages.OrchidPage

@Description(value = "Returns the URL to your site's homepage.", name = "Homepage URL")
class HomepageUrlFunction : TemplateFunction("homepageUrl", true) {

    override fun parameters(): Array<String> {
        return emptyArray()
    }

    override fun apply(context: OrchidContext, page: OrchidPage?): Any? {
        return context.findPage("home", "home", "home")?.link ?: context.baseUrl
    }
}
