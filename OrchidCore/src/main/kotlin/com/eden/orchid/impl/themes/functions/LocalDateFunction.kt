package com.eden.orchid.impl.themes.functions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.converters.DateTimeConverter
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault

import javax.inject.Inject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Description(value = "Format a Java 8 LocalDate object to String.", name = "Format LocalDate")
class LocalDateFunction @Inject
constructor(val context: OrchidContext, val converter: DateTimeConverter) : TemplateFunction("localDate", true) {

    @Option
    @Description("The object to format.")
    var input: Any? = null

    @Option
    @StringDefault("MMM d yyyy  hh:mm a")
    @Description("The date format to display the date as.")
    var format: String? = null

    override fun parameters(): Array<String> {
        return arrayOf("input", "format")
    }

    override fun apply(): Any {
        return DateTimeFormatter.ofPattern(format!!).format(converter.convert(LocalDate::class.java, input).second)
    }
}
