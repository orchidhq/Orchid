package com.eden.orchid.snippets.adapter

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.snippets.models.SnippetConfig
import com.eden.orchid.utilities.readToString

@Description("Scan through files using regex to locate multiple snippets embedded inside each")
class EmbeddedSnippetsAdapter : SnippetsAdapter {

    override fun getType(): String = "embedded"

    @Option
    @Description("The base directory in resources to look for snippets files in.")
    @StringDefault("snippets")
    lateinit var baseDirs: List<String>

    @Option
    @Description("File extensions to filter resources by.")
    lateinit var fileExtensions: List<String>

    @Option
    @BooleanDefault(true)
    @Description("Whether to search recursively")
    var recursive: Boolean = true

    @Option
    @StringDefault("""^.*?snippet::(.+?)\[(.*?)]\s*?${'$'}""")
    lateinit var startPattern: String

    @Option
    @StringDefault("""^.*?end::(.+?)?\s*?${'$'}""")
    lateinit var endPattern: String

    @Option
    @IntDefault(1)
    var patternNameGroup: Int = 1

    @Option
    @IntDefault(2)
    var patternTagGroup: Int = 2

    override fun addSnippets(context: OrchidContext): Sequence<SnippetConfig> = sequence {
        baseDirs.forEach { baseDir ->
            addSnippetsInDir(context, baseDir)
        }
    }

    private suspend fun SequenceScope<SnippetConfig>.addSnippetsInDir(context: OrchidContext, baseDir: String) {
        context
            .getResourceEntries(
                baseDir,
                fileExtensions.takeIf { it.isNotEmpty() }?.toTypedArray(),
                recursive,
                LocalResourceSource
            )
            .forEach {
                addSnippet(context, it)
            }
    }

    private suspend fun SequenceScope<SnippetConfig>.addSnippet(context: OrchidContext, resource: OrchidResource) {
        val tagStartRegex = startPattern.toRegex()
        val tagEndRegex = endPattern.toRegex()

        val content = resource.getContentStream().readToString() ?: ""

        val tagStack = mutableListOf<EmbeddedSnippet>()

        content.lineSequence().forEachIndexed { lineIndex, line ->
            val tagStartMatch = tagStartRegex.matchEntire(line)
            val tagEndMatch = tagEndRegex.matchEntire(line)

            if (tagStartMatch != null) {
                val snippetName = tagStartMatch.groupValues[patternNameGroup].trim()

                val tags = if (tagStartMatch.groupValues.size >= patternTagGroup + 1) {
                    tagStartMatch.groupValues[patternTagGroup].split(",").map { it.trim() }.dropWhile { it.isEmpty() }
                } else {
                    emptyList()
                }

                tagStack.add(EmbeddedSnippet(snippetName, tags))
            } else if (tagEndMatch != null) {
                val snippetName = tagEndMatch.groupValues[patternNameGroup].trim()

                val lastSnippet = if(tagStack.isNotEmpty()) {
                    tagStack.removeAt(tagStack.lastIndex)
                } else null

                if(lastSnippet != null) {
                    if(snippetName.isEmpty() || lastSnippet.name == snippetName) {
                        val snippetResource = StringResource(
                            OrchidReference(resource.reference),
                            lastSnippet.lines.joinToString(separator = "\n").trimIndent(),
                            null
                        )

                        yield(
                            SnippetConfig(
                                context,
                                lastSnippet.name,
                                lastSnippet.tags,
                                snippetResource
                            )
                        )
                    }
                    else {
                        Clog.e("Mismatched snippet groups at line $lineIndex: expected close of '${lastSnippet.name}', found '$snippetName'")
                    }
                }
            } else {
                if (tagStack.isNotEmpty()) {
                    tagStack.last().lines.add(line)
                }
            }
        }
    }

    private data class EmbeddedSnippet(
        val name: String,
        val tags: List<String>,
        val lines: MutableList<String> = mutableListOf()
    )
}
