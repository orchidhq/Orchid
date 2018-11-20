package com.eden.orchid.impl.themes.functions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault

import javax.inject.Inject

@Description(value = "Parse any String using any of Orchid's included data languages.", name = "Parse-As")
class ParseAsFunction
@Inject
constructor(
        private val context: OrchidContext
) : TemplateFunction("parseAs", false) {

    @Option
    @Description("The content to parse.")
    var input: Any? = null

    @Option
    @StringDefault("yaml")
    @Description("The extension to parse the inner content against.")
    lateinit var ext: String

    override fun parameters(): Array<String> {
        return arrayOf("input", "ext")
    }

    override fun apply(): Any {
        return context.parse(ext, input?.toString() ?: "")
    }
}