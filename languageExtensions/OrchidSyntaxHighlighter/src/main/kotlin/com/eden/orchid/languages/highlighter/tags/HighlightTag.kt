package com.eden.orchid.languages.highlighter.tags

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.utilities.encodeSpaces
import org.python.util.PythonInterpreter

@Description("Add syntax highlighting using Pygments.", name = "Highlight")
class HighlightTag : TemplateTag("highlight", Type.Content, true) {

    @Option
    @StringDefault("java")
    @Description("Your language to use for the syntax highlighting format.")
    lateinit var language: String

    override fun parameters() = arrayOf(::language.name)

    public fun highlight(input: String): String {
        try {
            val interpreter = PythonInterpreter()

            val pygmentsScript: OrchidResource? = context.getDefaultResourceSource(null, null).getResourceEntry(context, "scripts/pygments/pygments.py")
            val pythonScript = pygmentsScript?.content ?: ""

            // Set a variable with the content you want to work with
            interpreter.set("code", input)
            interpreter.set("codeLanguage", language)

            // Simply use Pygments as you would in Python
            interpreter.exec(pythonScript)
            var result = interpreter.get("result", String::class.java)

            // replace newlines with <br> tag, and spaces between tags with &nbsp; to preserve original structure
            return result.encodeSpaces()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

        return input
    }

}
