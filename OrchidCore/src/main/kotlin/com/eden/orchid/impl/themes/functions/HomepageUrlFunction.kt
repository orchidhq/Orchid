package com.eden.orchid.impl.themes.functions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.options.annotations.Description
import javax.inject.Inject

@Description(value = "Returns the URL to your site's homepage.", name = "Homepage URL")
class HomepageUrlFunction
@Inject
constructor(val context: OrchidContext) : TemplateFunction("homepageUrl", true) {

    override fun parameters(): Array<String> {
        return emptyArray()
    }

    override fun apply(): Any {
        return context.findPage("home", "home", "home")?.link ?: context.baseUrl
    }
}
