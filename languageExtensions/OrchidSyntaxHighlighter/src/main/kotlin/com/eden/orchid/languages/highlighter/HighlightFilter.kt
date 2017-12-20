package com.eden.orchid.languages.highlighter

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource
import org.jtwig.functions.FunctionRequest
import org.jtwig.functions.JtwigFunction
import org.python.util.PythonInterpreter
import javax.inject.Inject

class HighlightFilter @Inject
constructor(private val context: OrchidContext) : JtwigFunction {

    override fun name(): String {
        return "highlight"
    }

    override fun aliases(): Collection<String> {
        return emptyList()
    }

    override fun execute(request: FunctionRequest): Any? {
        val fnParams = request
                .minimumNumberOfArguments(1)
                .maximumNumberOfArguments(2)
                .arguments

        if (fnParams.size == 1) {
            val input = fnParams[0].toString()
            return apply(input, null)
        } else if (fnParams.size == 2) {
            val input = fnParams[0].toString()
            val language = fnParams[1].toString()
            return apply(input, language)
        } else {
            return null
        }
    }

    fun apply(input: String, language: String?): Any {
        try {
            val interpreter = PythonInterpreter()

            val pygmentsScript: OrchidResource? = context.getResourceEntry("scripts/pygments/pygments.py")

            val pythonScript = pygmentsScript?.content ?: ""

            // Set a variable with the content you want to work with
            interpreter.set("code", input)
            interpreter.set("codeLanguage", if (!EdenUtils.isEmpty(language)) language else "java")

            // Simple use Pygments as you would in Python
            interpreter.exec(pythonScript)

            return interpreter.get("result", String::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return input
    }
}