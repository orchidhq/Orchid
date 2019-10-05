package com.eden.orchid.impl.compilers.parsers

import com.eden.orchid.api.compilers.OrchidParser
import com.eden.orchid.utilities.logSyntaxError
import org.json.JSONArray
import org.json.JSONObject
import java.util.regex.Pattern
import javax.inject.Inject

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
            val errorMessage = e.message ?: ""

            if(!errorMessage.startsWith(JSON_OBJECT_MESSAGE_PREFIX)) {
                // this is a syntax error, not simply that we are parsing an array
                logJsonSyntaxError(extension, input, errorMessage)
                return null
            }
        }

        // If it fails to parse as JSON Object and throws exception, try again as JSON Array
        try {
            val jsonObject = JSONObject()
            jsonObject.put(OrchidParser.arrayAsObjectKey, JSONArray(input))
            return jsonObject.toMap()
        } catch (e: Exception) {
            logJsonSyntaxError(extension, input, e.message ?: "")
        }

        return null
    }

    private fun logJsonSyntaxError(extension: String, input: String, errorMessage: String) {
        val lineNumberMatch = "^.*?\\[character (\\d+) line (\\d+)]$"
            .toRegex()
            .matchEntire(errorMessage)
            ?.groupValues

        val lineNumber = lineNumberMatch?.get(2)?.toIntOrNull() ?: 0
        val lineColumn = lineNumberMatch?.get(1)?.toIntOrNull() ?: 0

        input.logSyntaxError(extension, lineNumber, lineColumn, errorMessage)
    }

    override fun serialize(extension: String, input: Any): String {
        return if (input is Map<*, *>) {
            JSONObject(input).toString()
        } else {
            JSONObject().toString()
        }
    }

    companion object {
        const val JSON_OBJECT_MESSAGE_PREFIX = "A JSONObject text must begin with '{'"
    }
}
