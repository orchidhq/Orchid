package com.eden.orchid.writersblocks.functions

import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.converters.StringConverter
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import org.atteo.evo.inflector.English
import javax.inject.Inject

class PluralizeFunction @Inject
constructor(private val converter: StringConverter) : TemplateFunction("pluralize", true) {

    @Option
    lateinit var input: String

    @Option @IntDefault(Int.MIN_VALUE)
    var count: Int = 0

    override fun parameters(): Array<String> {
        return arrayOf("input", "count")
    }

    override fun apply(input: Any?): Any {
        val actualInput = converter.convert(input ?: this.input)

        return if (this.count != Int.MIN_VALUE) {
            English.plural(actualInput.second, this.count)
        } else English.plural(actualInput.second)
    }
}
