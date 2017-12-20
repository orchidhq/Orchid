package com.eden.orchid.languages.highlighter

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.mitchellbosecke.pebble.extension.Filter
import com.mitchellbosecke.pebble.extension.escaper.SafeString
import org.jtwig.functions.FunctionRequest
import org.jtwig.functions.JtwigFunction
import java.util.*
import javax.inject.Inject

class HighlightPrismFilter @Inject
constructor(private val context: OrchidContext) : JtwigFunction, Filter {

    override fun name(): String {
        return "highlightPrism"
    }

    override fun aliases(): Collection<String> {
        return emptyList()
    }

    override fun execute(request: FunctionRequest): Any? {
        val fnParams = request
                .minimumNumberOfArguments(1)
                .maximumNumberOfArguments(3)
                .arguments

        if (fnParams.size == 1) {
            val input = fnParams[0].toString()
            return apply(input, null, null)
        } else if (fnParams.size == 2) {
            val input = fnParams[0].toString()
            val language = fnParams[1].toString()
            return apply(input, language, null)
        } else if (fnParams.size == 3) {
            val input = fnParams[0].toString()
            val language = fnParams[1].toString()
            val markedUpLines = fnParams[2].toString()
            return apply(input, language, markedUpLines)
        } else {
            return null
        }
    }

    override fun apply(input: Any, args: Map<String, Any>): Any {
        return SafeString(apply(
                input.toString(),
                args.getOrDefault("language", "text") as String?,
                args.getOrDefault("lines", null) as String?))
    }

    override fun getArgumentNames(): List<String> {
        return Arrays.asList("language", "lines")
    }

    fun apply(input: String, language: String?, markedUpLines: String?): String {
        var actualLanguage = language
        var actualMarkedUpLines = markedUpLines
        if (actualLanguage != null) {
            actualLanguage = Clog.format("class=\"language-{} line-numbers\"", actualLanguage)
        } else {
            actualLanguage = Clog.format("class=\"language-markup line-numbers\"")
        }
        if (actualMarkedUpLines != null) {
            actualMarkedUpLines = Clog.format("data-line=\"{}\"", actualMarkedUpLines)
        } else {
            actualMarkedUpLines = ""
        }

        return Clog.format("<pre #{$1} #{$2}><code #{$1}>#{$3}</code></pre>", actualLanguage, actualMarkedUpLines, input.trim { it <= ' ' })
    }
}