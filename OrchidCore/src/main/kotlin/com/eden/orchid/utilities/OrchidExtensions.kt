package com.eden.orchid.utilities

import com.caseyjbrooks.clog.Clog
import com.eden.common.json.JSONElement
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.registration.OrchidModule
import com.google.inject.binder.LinkedBindingBuilder
import org.apache.commons.lang3.StringUtils
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList
import java.util.regex.Pattern
import kotlin.reflect.KClass

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

fun String.logSyntaxError(extension: String, lineNumber: Int, errorMessage: String = "") {
    val lines = this.lines()
    val linesBeforeStart = Math.max(lineNumber - 3, 0)
    val linesBeforeEnd = Math.max(lineNumber - 1, 0)
    val linesAfterStart = lineNumber
    val linesAfterEnd = Math.min(lineNumber + 5, lines.size)

    val linesBefore = lines.subList(linesBeforeStart, linesBeforeEnd)
    val errorLine = lines.get(linesBeforeEnd)
    val linesAfter = lines.subList(linesAfterStart, linesAfterEnd)

    var templateSnippet = ".{} Syntax Error: {} (see source below)"
    templateSnippet += "\n   |" + linesBefore.joinToString("\n   |")
    templateSnippet += "\n#{$0|fg('RED')}-->|#{$0|reset}$errorLine"
    templateSnippet += "\n   |" + linesAfter.joinToString("\n   |")

    Clog.tag("Syntax error").e(templateSnippet, extension.toUpperCase(), errorMessage)
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
infix fun String.from(mapper: String.() -> Array<String>): Array<String> {
    return mapper(this)
}

infix fun Array<String>.to(mapper: Array<String>.() -> String): String {
    return mapper(this)
}

infix fun Array<String>.with(mapper: String.() -> String): Array<String> {
    return this.map { mapper(it) }.toTypedArray()
}

// "from" mappers
fun String.camelCase(): Array<String> {
    return StringUtils.splitByCharacterTypeCamelCase(this)
}

fun String.camelCase(mapper: String.() -> String): Array<String> {
    return camelCase().with(mapper)
}

fun String.words(): Array<String> {
    return StringUtils.splitByWholeSeparator(this, null)
}

fun String.words(mapper: String.() -> String): Array<String> {
    return words().with(mapper)
}

fun String.snakeCase(): Array<String> {
    return StringUtils.splitByWholeSeparator(this, "_")
}

fun String.snakeCase(mapper: String.() -> String): Array<String> {
    return snakeCase().with(mapper)
}

fun String.dashCase(): Array<String> {
    return StringUtils.splitByWholeSeparator(this, "-")
}

fun String.dashCase(mapper: String.() -> String): Array<String> {
    return dashCase().with(mapper)
}

fun String.filename(): Array<String> {
    return this
        .words()
        .flatMap {
            it.dashCase().toList()
        }
        .flatMap {
            it.snakeCase().toList()
        }
        .flatMap {
            it.camelCase().toList()
        }
        .toTypedArray()
}

fun String.filename(mapper: String.() -> String): Array<String> {
    return filename().with(mapper)
}

// "to" mappers
fun Array<String>.pascalCase(): String {
    return map { it.toLowerCase().capitalize() }.joinToString(separator = "")
}

fun List<String>.pascalCase(): String {
    return toTypedArray().pascalCase()
}

fun Array<String>.pascalCase(mapper: String.() -> String): String {
    return map(mapper).pascalCase()
}

fun Array<String>.camelCase(): String {
    return pascalCase().decapitalize()
}

fun List<String>.camelCase(): String {
    return toTypedArray().camelCase()
}

fun Array<String>.camelCase(mapper: String.() -> String): String {
    return map(mapper).camelCase()
}

fun Array<String>.words(): String {
    return joinToString(separator = " ")
}

fun List<String>.words(): String {
    return toTypedArray().words()
}

fun Array<String>.words(mapper: String.() -> String): String {
    return map(mapper).words()
}

fun Array<String>.snakeCase(): String {
    return map { it.toUpperCase() }.joinToString(separator = "_")
}

fun List<String>.snakeCase(): String {
    return toTypedArray().snakeCase()
}

fun Array<String>.snakeCase(mapper: String.() -> String): String {
    return map(mapper).snakeCase()
}

fun Array<String>.dashCase(): String {
    return joinToString(separator = "-")
}

fun List<String>.dashCase(): String {
    return toTypedArray().dashCase()
}

fun Array<String>.dashCase(mapper: String.() -> String): String {
    return map(mapper).dashCase()
}

fun Array<String>.slug(): String {
    return dashCase().toLowerCase()
}

fun List<String>.slug(): String {
    return toTypedArray().slug()
}

fun Array<String>.slug(mapper: String.() -> String): String {
    return map(mapper).slug()
}

fun Array<String>.titleCase(): String {
    return joinToString(separator = " ", transform = { it.capitalize() })
}

fun List<String>.titleCase(): String {
    return toTypedArray().titleCase()
}

fun Array<String>.titleCase(mapper: String.() -> String): String {
    return map(mapper).titleCase()
}

// "with" mappers
fun String.urlSafe(): String {
    return replace("\\s+".toRegex(), "-").replace("[^\\w-_]".toRegex(), "")
}

fun String.urlSafe(mapper: String.() -> String): String {
    return urlSafe().mapper()
}

// Better Kotlin Module registration
//----------------------------------------------------------------------------------------------------------------------

// bind
inline fun <reified T : Any> OrchidModule.bind(): LinkedBindingBuilder<T> {
    return this._bind(T::class.java)
}

// addToSet
inline fun <reified T : Any> OrchidModule.addToSet(vararg objectClasses: KClass<out T>) {
    this.addToSet(T::class.java, *(objectClasses.map { it.java }.toTypedArray()))
}

inline fun <reified T : Any, reified IMPL : T> OrchidModule.addToSet() {
    this.addToSet(T::class.java, IMPL::class.java)
}

inline fun <reified T : Any> OrchidModule.addToSet(vararg objects: T) {
    this.addToSet(T::class.java, *objects)
}

// Better dynamic object resolution
//----------------------------------------------------------------------------------------------------------------------

inline fun <reified T : Any> OrchidContext.resolve(): T {
    return this.resolve(T::class.java)
}

inline fun <reified T : Any> OrchidContext.resolveSet(): Set<T> {
    return this.resolveSet(T::class.java)
}

fun Number.makeMillisReadable(): String {
    val lMillis = this.toDouble()
    val hours: Int
    val minutes: Int
    val seconds: Int
    val millis: Int
    val sTime: String
    seconds = (lMillis / 1000).toInt() % 60
    millis = (lMillis % 1000).toInt()
    if (seconds > 0) {
        minutes = (lMillis / 1000.0 / 60.0).toInt() % 60
        if (minutes > 0) {
            hours = (lMillis / 1000.0 / 60.0 / 60.0).toInt() % 24
            if (hours > 0) {
                sTime = hours.toString() + "h " + minutes + "m " + seconds + "s " + millis + "ms"
            } else {
                sTime = minutes.toString() + "m " + seconds + "s " + millis + "ms"
            }
        } else {
            sTime = seconds.toString() + "s " + millis + "ms"
        }
    } else {
        sTime = millis.toString() + "ms"
    }
    return sTime
}
