package com.eden.orchid.impl.themes.functions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault

import javax.inject.Inject

@Description(value = "Compile any String using any of Orchid's included markup languages.", name = "Compile-As")
class CompileAsFunction @Inject
constructor(private val context: OrchidContext) : TemplateFunction("compileAs", true) {

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

    override fun apply(): Any {
        return context.compile(ext, input?.toString() ?: "")
    }
}
