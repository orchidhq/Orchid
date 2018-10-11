package com.eden.orchid.impl.compilers.parsers

import com.eden.orchid.api.compilers.OrchidParser
import org.json.JSONArray
import org.json.JSONObject

import javax.inject.Inject
import java.util.regex.Pattern

class JsonParser @Inject
constructor() : OrchidParser(100) {

    override fun getDelimiter(): String {
        return Pattern.quote(delimiterString)
    }

    override fun getDelimiterString(): String {
        return ";"
    }

    override fun getSourceExtensions(): Array<String> {
        return arrayOf("json")
    }

    override fun parse(extension: String, input: String): Map<String, Any>? {
        // first try parsing it as JSON Object
        try {
            return JSONObject(input).toMap()
        } catch (e: Exception) {
        }

        // If it fails to parse as JSON Object and throws exception, try again as JSON Array
        try {
            val `object` = JSONObject()
            `object`.put(OrchidParser.arrayAsObjectKey, JSONArray(input))
            return `object`.toMap()
        } catch (e: Exception) {
        }

        return null
    }

    override fun serialize(extension: String, input: Any): String {
        return JSONObject(input).toString()
    }
}
