package com.eden.orchid.impl.themes.functions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.utilities.OrchidUtils
import javax.inject.Inject

@Description(value = "Appends the site base URL to a String.", name = "Base URL")
class BaseUrlFunction
@Inject
constructor(val context: OrchidContext) : TemplateFunction("baseUrl", true) {

    @Option
    @Description("The text to append the site base URL to.")
    lateinit var input: String

    override fun parameters(): Array<String> {
        return arrayOf("input")
    }

    override fun apply(): Any {
        val cleanedBaseUrl = OrchidUtils.normalizePath(context.baseUrl)
        val cleanedInput = OrchidUtils.normalizePath(input)

        return "$cleanedBaseUrl/$cleanedInput"
    }
}
