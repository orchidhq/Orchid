package com.eden.orchid.writersblocks.functions

import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.converters.StringConverter
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.utilities.encodeSpaces
import javax.inject.Inject

class EncodeSpacesFunction @Inject
constructor(private val converter: StringConverter) : TemplateFunction("encodeSpaces", true) {

    @Option
    @Description("The input to encode.")
    lateinit var input: String

    override fun parameters(): Array<String> {
        return arrayOf("input")
    }

    override fun apply(): Any {
        return converter.convert(String::class.java, input).second.encodeSpaces()
    }
}
