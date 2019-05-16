package com.eden.orchid.impl.compilers.parsers

import com.eden.orchid.api.compilers.OrchidParser
import com.eden.orchid.utilities.SuppressedWarnings
import java.io.StringReader
import java.io.StringWriter
import java.util.Properties
import javax.inject.Inject

class PropertiesParser @Inject
constructor() : OrchidParser(50) {

    override fun getDelimiter(): String {
        return "~"
    }

    override fun getSourceExtensions(): Array<String> {
        return arrayOf("properties", "prop")
    }

    @Suppress(SuppressedWarnings.UNCHECKED_KOTLIN)
    override fun parse(extension: String, input: String): Map<String, Any>? {
        try {
            val prop = Properties()
            prop.load(StringReader(input))

            val mapOfProperties = mutableMapOf<String, Any>()
            for((k, v) in prop){
                mapOfProperties[k.toString()] = v
            }

            return mapOfProperties
        } catch (e: Exception) {
        }

        return emptyMap()
    }

    override fun serialize(extension: String, input: Any): String {
        if(input is Map<*, *>) {
            val prop = Properties()
            prop.putAll(input)
            val writer = StringWriter()
            prop.store(writer, null)

            return writer.toString()
        }

        return ""
    }
}
