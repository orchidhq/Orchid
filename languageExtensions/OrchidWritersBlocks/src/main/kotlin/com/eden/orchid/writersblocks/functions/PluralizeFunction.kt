package com.eden.orchid.writersblocks.functions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.converters.StringConverter
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.pages.OrchidPage
import org.atteo.evo.inflector.English
import javax.inject.Inject

@Description("Take any word and make it plural.", name = "Pluralize")
class PluralizeFunction
@Inject
constructor(
        private val converter: StringConverter
) : TemplateFunction("pluralize", true) {

    @Option
    @Description("The input String to pluralize.")
    lateinit var input: String

    @Option @IntDefault(Int.MIN_VALUE)
    @Description("How many of the item is present.")
    var count: Int = 0

    override fun parameters(): Array<String> {
        return arrayOf("input", "count")
    }

    override fun apply(context: OrchidContext, page: OrchidPage?): Any? {
        val actualInput = converter.convert(String::class.java, input)

        return if (this.count != Int.MIN_VALUE) {
            English.plural(actualInput.second, this.count)
        } else English.plural(actualInput.second)
    }
}
