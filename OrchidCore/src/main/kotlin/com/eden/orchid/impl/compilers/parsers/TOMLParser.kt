package com.eden.orchid.impl.compilers.parsers

import com.eden.orchid.api.compilers.OrchidParser
import com.eden.orchid.utilities.logSyntaxError
import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import java.util.regex.Pattern
import javax.inject.Inject

class TOMLParser
@Inject
constructor() : OrchidParser(100) {

    override fun getDelimiter(): String {
        return Pattern.quote(delimiterString)
    }

    override fun getDelimiterString(): String {
        return "+"
    }

    override fun getSourceExtensions(): Array<String> {
        return arrayOf("tml", "toml")
    }

    override fun parse(extension: String, input: String): Map<String, Any>? {
        try {
            val toml = Toml().read(input)
            return toml.toMap()
        }
        catch (e: Exception) {
            val errorMessage = e.message ?: ""

            val lineNumber = "^.*?line (\\d+):.*$"
                .toRegex()
                .matchEntire(errorMessage)
                ?.groupValues
                ?.get(1)
                ?.toIntOrNull()
                ?: 0

            input.logSyntaxError(extension, lineNumber, 0, errorMessage)
        }

        return null
    }

    override fun serialize(extension: String, input: Any?): String {
        val tomlWriter = TomlWriter()
        return tomlWriter.write(input)
    }
}
