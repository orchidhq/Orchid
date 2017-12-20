package com.eden.orchid.languages.diagrams

import com.eden.orchid.api.compilers.OrchidCompiler
import net.sourceforge.plantuml.FileFormat
import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.SourceStringReader
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.Charset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlantUmlCompiler @Inject
constructor() : OrchidCompiler(800) {

    private val tags = arrayOf("uml", "salt", "math", "latex", "gantt")

    override fun compile(extension: String, source: String, data: Map<String, Any>?): String {
        var result = source
        try {
            try {
                // ensure string is wrapped in @startuml...@enduml
                result = result.trim { it <= ' ' }
                result = wrapDiagram(result)

                // compile string to SVG
                val os = ByteArrayOutputStream(1024)

                val fileFormat = FileFormatOption(FileFormat.SVG)
                fileFormat.hideMetadata()

                val reader = SourceStringReader(result)
                reader.outputImage(os, fileFormat)
                os.close()

                var s = String(os.toByteArray(), Charset.forName("UTF-8"))
                s = s.replace("<\\?(.*)\\?>".toRegex(), "") // remove XML declaration

                return s
            } catch (e: IOException) {
                e.printStackTrace()
                return ""
            }

        } catch (e: Throwable) {
            e.printStackTrace()
            return ""
        }
    }

    override fun getOutputExtension(): String {
        return "svg"
    }

    override fun getSourceExtensions(): Array<String> {
        return arrayOf("uml")
    }

    private fun wrapDiagram(input: String): String {
        var isWrapped = false

        var source = input

        for (tag in tags) {
            if (source.startsWith("@start" + tag)) {
                isWrapped = true
                break
            }
            if (source.endsWith("@end" + tag)) {
                isWrapped = true
                break
            }
        }

        if (!isWrapped) {
            source = "@startuml\n" + source
            source = source + "\n@enduml"
        }

        return source
    }
}
