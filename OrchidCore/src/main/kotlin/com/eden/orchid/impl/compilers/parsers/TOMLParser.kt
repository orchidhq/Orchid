package com.eden.orchid.impl.compilers.parsers

import com.eden.orchid.api.compilers.OrchidParser
import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter

import javax.inject.Inject
import java.util.regex.Pattern

class TOMLParser @Inject
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

    override fun parse(extension: String, input: String): Map<String, Any> {
        val toml = Toml().read(input)
        return toml.toMap()
    }

    override fun serialize(extension: String, input: Any): String {
        val tomlWriter = TomlWriter()
        return tomlWriter.write(input)
    }
}
