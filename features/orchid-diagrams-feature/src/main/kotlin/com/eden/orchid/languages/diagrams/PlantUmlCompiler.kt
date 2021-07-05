package com.eden.orchid.languages.diagrams

import com.eden.orchid.api.compilers.OrchidCompiler
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.OrchidResource
import net.sourceforge.plantuml.FileFormat
import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.SourceStringReader
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Archetype(value = ConfigArchetype::class, key = "services.compilers.puml")
class PlantUmlCompiler
@Inject
constructor() : OrchidCompiler(800) {

    private val tags = arrayOf("uml", "salt", "math", "latex", "gantt")

    // TODO: bundle a DOT executable and use GraphvizUtils.setDotExecutable() to use the bundled one (minimize system dependencies needed)

    override fun compile(
        os: OutputStream,
        resource: OrchidResource?,
        extension: String,
        input: String,
        data: MutableMap<String, Any>?
    ) {
        OutputStreamWriter(os).append(compileMultipleDiagrams(input)).close()
    }

    override fun getOutputExtension(): String {
        return "svg"
    }

    override fun getSourceExtensions(): Array<String> {
        return arrayOf("uml", "puml")
    }

    private fun wrapDiagram(input: String): String {
        var isWrapped = false

        val source = input.trim()

        for (tag in tags) {
            if (source.contains("@start$tag") && source.contains("@end$tag")) {
                isWrapped = true
                break
            }
        }

        return if (!isWrapped) "@startuml\n$source\n@enduml" else source
    }

    private fun compileMultipleDiagrams(input: String): String {
        var formattedInput = wrapDiagram(input.trim())

        for (tag in tags) {
            val templateResult = StringBuffer(formattedInput.length)

            val matcher = "@start$tag(.+?)@end$tag"
                .toRegex(setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.MULTILINE, RegexOption.IGNORE_CASE))
                .toPattern()
                .matcher(formattedInput)
            while (matcher.find()) {
                val content = matcher.group(1)
                val replacement = compileSingleDiagram(content, tag)
                matcher.appendReplacement(templateResult, "")
                templateResult.append(replacement)
            }
            matcher.appendTail(templateResult)

            formattedInput = templateResult.toString()
        }

        return formattedInput
    }

    private fun compileSingleDiagram(input: String, tag: String): String {
        val formattedInput = """
            @start$tag
            ${input.trim()}
            @end$tag
        """.trimIndent()

        try {
            // compile string to SVG
            val os = ByteArrayOutputStream(1024)

            val fileFormat = FileFormatOption(FileFormat.SVG)
            fileFormat.hideMetadata()

            val reader = SourceStringReader(formattedInput, "UTF-8")
            reader.outputImage(os, fileFormat)
            os.close()

            var s = String(os.toByteArray(), StandardCharsets.UTF_8)
            s = s.replace("<\\?(.*)\\?>".toRegex(), "") // remove XML declaration

            return s
        } catch (e: IOException) {
            e.printStackTrace()
            return ""
        }
    }
}
