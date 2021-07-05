package com.eden.orchid.impl.themes.functions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.converters.DateTimeConverter
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.utilities.resolve
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Description(value = "Format a Java 8 LocalDate object to String.", name = "Format LocalDate")
class LocalDateFunction : TemplateFunction("localDate", true) {

    @Option
    @Description("The object to format.")
    var input: Any? = null

    @Option
    @StringDefault("MMM d yyyy  hh:mm a")
    @Description("The date format to display the date as.")
    lateinit var format: String

    override fun parameters() = arrayOf(::input.name, ::format.name)

    override fun apply(context: OrchidContext, page: OrchidPage?): Any? {
        val converter: DateTimeConverter = context.resolve()
        return DateTimeFormatter.ofPattern(format).format(converter.convert(LocalDate::class.java, input).second)
    }
}
