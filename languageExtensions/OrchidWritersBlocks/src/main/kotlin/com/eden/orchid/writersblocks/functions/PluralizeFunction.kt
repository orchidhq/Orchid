package com.eden.orchid.writersblocks.functions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.converters.StringConverter
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.utilities.resolve
import org.atteo.evo.inflector.English

@Description("Take any word and make it plural.", name = "Pluralize")
class PluralizeFunction : TemplateFunction("pluralize", true) {

    @Option
    @Description("The input String to pluralize.")
    lateinit var input: String

    @Option @IntDefault(Int.MIN_VALUE)
    @Description("How many of the item is present.")
    var count: Int = Int.MIN_VALUE

    override fun parameters() = arrayOf(::input.name, ::count.name)

    override fun apply(context: OrchidContext, page: OrchidPage?): Any? {
        return context
            .resolve<StringConverter>()
            .convert(String::class.java, input)
            .second
            .let { if (this.count != Int.MIN_VALUE) English.plural(it, this.count) else English.plural(it) }
    }
}
