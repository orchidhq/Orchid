package com.eden.orchid.utilities

import com.eden.common.json.JSONElement
import com.eden.common.util.EdenUtils
import org.apache.commons.lang3.StringUtils
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import java.util.regex.Pattern

fun String?.empty(): Boolean {
    return EdenUtils.isEmpty(this)
}

fun String?.wrap(width: Int = 80): List<String> {
    val matchList = ArrayList<String>()
    if (this != null) {
        if (!this.empty()) {
            val regex = Pattern.compile("(.{1,$width}(?:\\s|$))|(.{0,$width})", Pattern.DOTALL)
            val regexMatcher = regex.matcher(this)
            while (regexMatcher.find()) {
                val line = regexMatcher.group().trim { it <= ' ' }
                if (!EdenUtils.isEmpty(line)) {
                    matchList.add(line)
                }
            }
        }
    }

    return matchList
}

fun JSONElement?.isObject(): Boolean {
    return this != null && this.element != null && this.element is JSONObject
}

fun JSONElement?.isArray(): Boolean {
    return this != null && this.element != null && this.element is JSONArray
}

fun JSONElement?.isString(): Boolean {
    return this != null && this.element != null && this.element is String
}


// string conversions
fun String.from(mapper: (String) -> Array<String>): Array<String> {
    return mapper(this)
}

fun Array<String>.to(mapper: (Array<String>) -> String): String {
    return mapper(this)
}

fun Array<String>.with(mapper: (Array<String>) -> Array<String>): Array<String> {
    return mapper(this)
}

// "from" mappers
fun String.camelCase(): Array<String> {
    return StringUtils.splitByCharacterTypeCamelCase(this)
}

fun String.words(): Array<String> {
    return StringUtils.splitByWholeSeparator(this, null)
}

fun String.snakeCase(): Array<String> {
    return StringUtils.splitByWholeSeparator(this, "_")
}

fun String.dashCase(): Array<String> {
    return StringUtils.splitByWholeSeparator(this, "-")
}

// "to" mappers
fun Array<String>.pascalCase(): String {
    return this.map { it.toLowerCase().capitalize() }.joinToString(separator = "")
}

fun Array<String>.camelCase(): String {
    return this.pascalCase().decapitalize()
}

fun Array<String>.words(): String {
    return this.joinToString(separator = " ")
}

fun Array<String>.snakeCase(): String {
    return this.map { it.toUpperCase() }.joinToString(separator = "_")
}

fun Array<String>.dashCase(): String {
    return this.joinToString(separator = "-")
}

fun Array<String>.slug(): String {
    return this.dashCase().toLowerCase()
}

// "with" mappers
fun Array<String>.urlSafe(): Array<String> {
    return this.map {
        it.replace("\\s+".toRegex(), "-").replace("[^\\w-_]".toRegex(), "")
    }.toTypedArray()
}





