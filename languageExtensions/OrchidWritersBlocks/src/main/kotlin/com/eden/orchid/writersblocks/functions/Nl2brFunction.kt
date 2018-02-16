package com.eden.orchid.writersblocks.functions

import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.converters.StringConverter
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.utilities.nl2br
import javax.inject.Inject

class Nl2brFunction @Inject
constructor(private val converter: StringConverter) : TemplateFunction("nl2br", true) {

    @Option
    lateinit var input: String

    override fun parameters(): Array<String> {
        return arrayOf("input")
    }

    override fun apply(input: Any?): Any {
        return converter.convert(input ?: this.input).second.nl2br()
    }
}
