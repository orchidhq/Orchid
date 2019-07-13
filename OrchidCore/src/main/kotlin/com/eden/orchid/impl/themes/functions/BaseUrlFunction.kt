package com.eden.orchid.impl.themes.functions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.utilities.OrchidUtils

@Description(value = "Appends the site base URL to a String.", name = "Base URL")
class BaseUrlFunction : TemplateFunction("baseUrl", true) {

    @Option
    @Description("The text to append the site base URL to.")
    lateinit var input: String

    override fun parameters(): Array<String> {
        return arrayOf("input")
    }

    override fun apply(context: OrchidContext, page: OrchidPage?): Any? {
        val cleanedInput = OrchidUtils.normalizePath(input)

        return if(context.baseUrl == "/") {
            "/$cleanedInput"
        }
        else {
            "${context.baseUrl}/$cleanedInput"
        }
    }
}
