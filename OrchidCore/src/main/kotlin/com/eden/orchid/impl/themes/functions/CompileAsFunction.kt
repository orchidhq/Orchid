package com.eden.orchid.impl.themes.functions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.pages.OrchidPage

@Description(value = "Compile any String using any of Orchid's included markup languages.", name = "Compile-As")
class CompileAsFunction : TemplateFunction("compileAs", true) {

    @Option
    @Description("The content to compile.")
    var input: Any? = null

    @Option
    @StringDefault("txt")
    @Description("The extension to compile the inner content against.")
    lateinit var ext: String

    override fun parameters(): Array<String> {
        return arrayOf("input", "ext")
    }

    override fun apply(context: OrchidContext, page: OrchidPage?): Any? {
        return context.compile(ext, input?.toString() ?: "")
    }
}
