package com.eden.orchid.writersblocks.functions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.converters.StringConverter
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.utilities.encodeSpaces
import com.eden.orchid.utilities.resolve

@Description("Encode space characters as `&nbsp;` to preserve indentation.", name = "Encode Spaces")
class EncodeSpacesFunction : TemplateFunction("encodeSpaces", true) {

    @Option
    @Description("The input to encode.")
    lateinit var input: String

    override fun parameters() = arrayOf(::input.name)

    override fun apply(context: OrchidContext, page: OrchidPage?): Any? {
        return context
            .resolve<StringConverter>()
            .convert(String::class.java, input)
            .second
            .encodeSpaces()
    }

    private fun String.printBytes(): String = this.toByteArray().joinToString { "${it.toInt()}" }
}
