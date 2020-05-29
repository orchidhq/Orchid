package com.eden.orchid.languages.asciidoc.extensions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.readToString
import org.asciidoctor.ast.Document
import org.asciidoctor.extension.IncludeProcessor
import org.asciidoctor.extension.PreprocessorReader
import java.io.File
import javax.inject.Inject
import javax.inject.Provider
import kotlin.math.abs

class AsciidocIncludeProcessor
@Inject
constructor(
    private var contextProvider: Provider<OrchidContext>
) : IncludeProcessor() {

    private val context by lazy { contextProvider.get() }

    override fun process(
        document: Document?,
        reader: PreprocessorReader?,
        target: String?,
        attributes: MutableMap<String, Any>?
    ) {
        // first try looking for the target in normal site resources
        val resolvedResource = context.getDefaultResourceSource(LocalResourceSource, null).getResourceEntry(context, target ?: "")
        if (resolvedResource != null) {
            handleIncludeContent(
                reader,
                target,
                attributes,
                resolvedResource.reference.originalFullFileName,
                resolvedResource.getContentStream().readToString() ?: ""
            )
            return
        }

        // first try looking for the target in a normal file, relative to the specified docDir
        val docDir: String? = document?.attributes?.get("docdir")?.toString()
        if (docDir != null) {
            val baseDir = File(docDir)
            val resolvedFile = baseDir.resolve(target ?: "")

            if (resolvedFile.exists()) {
                handleIncludeContent(
                    reader,
                    target,
                    attributes,
                    resolvedFile.absolutePath,
                    resolvedFile.bufferedReader().readText()
                )
                return
            }
        }
    }

    private fun handleIncludeContent(
        reader: PreprocessorReader?,
        target: String?,
        attributes: MutableMap<String, Any>?,
        resolvedPath: String?,
        resolvedContent: String
    ) {
        val actualContentToInclude = if (attributes != null) {
            if (attributes.contains("tag")) {
                getIncludedTags(resolvedContent, attributes["tag"]!!.toString(), true)
            } else if (attributes.contains("tags")) {
                getIncludedTags(resolvedContent, attributes["tags"]!!.toString(), false)
            } else if (attributes.contains("lines")) {
                getIncludedLines(resolvedContent, attributes["lines"]!!.toString())
            } else {
                null
            }
        } else {
            null
        } ?: resolvedContent

        reader?.push_include(
            actualContentToInclude,
            target,
            resolvedPath,
            1,
            attributes
        )
    }

    override fun handles(target: String?): Boolean {
        // if there's a file relative to the base dir, don't handle it here. Let Asciidoctor do that, which preserves
        // all the attrs it can normally handle. Only if a file cannot be found, should we attempt to handle it, and
        // then we can check Orchid resources as a fallback

        return when {
            OrchidUtils.isExternal(target) -> false
            else -> true
        }
    }


// Include by lines
//----------------------------------------------------------------------------------------------------------------------

    fun getActualLines(lines: List<String>, attribute: String): List<Int> {
        val size = lines.size
        val groups = attribute.split(",").flatMap { it.split(";") }

        val actualLines = mutableListOf<Int>()

        groups.forEach {
            if (it.contains("..")) {
                val (rangeStart, rangeEnd) = it.split("..")

                val requestedStart = rangeStart.trim().toInt()
                val requestedEnd = rangeEnd.trim().toInt()

                val actualStart = if (requestedStart < 0) size - (abs(requestedStart) - 1) else requestedStart
                val actualEnd = if (requestedEnd < 0) size - (abs(requestedEnd) - 1) else requestedEnd

                actualLines.addAll(actualStart..actualEnd)
            } else {
                val requestedIt = it.trim().toInt()
                val actualIt = if (requestedIt < 0) size - (abs(requestedIt) - 1) else requestedIt

                actualLines.add(actualIt)
            }
        }

        return actualLines.sorted()
    }

    fun getIncludedLines(resolvedContent: String, attribute: String): String {
        val lines = resolvedContent.lines()
        val requestedLines = getActualLines(lines, attribute)

        return lines
            .filterIndexed { index, _ -> (index + 1) in requestedLines }
            .joinToString(separator = "\n")
    }

// Include by tags
//----------------------------------------------------------------------------------------------------------------------

    fun getIncludedTags(resolvedContent: String, attribute: String, single: Boolean): String {
        val taggedSections = getTaggedSections(resolvedContent)
        val requestedTags = getActualTags(taggedSections, attribute, single)
        val content = getTagContent(taggedSections, requestedTags)
        return content
    }

    fun getTaggedSections(resolvedContent: String): Map<String, List<String>> {
        val tagStack = mutableListOf<Pair<String, MutableList<String>>>()
        val closedTagStack = mutableListOf<Pair<String, MutableList<String>>>()

        resolvedContent.lineSequence().forEach { line ->
            val tagStartMatch = tagStartRegex.matchEntire(line)
            val tagEndMatch = tagEndRegex.matchEntire(line)

            if (tagStartMatch != null) {
                val tagName = tagStartMatch.groupValues[1]
                tagStack.add(tagName to mutableListOf())
            } else if (tagEndMatch != null) {
                val last = tagStack.removeAt(tagStack.lastIndex)
                closedTagStack.add(last)
            } else {
                if (tagStack.isNotEmpty()) {
                    tagStack.last().second.add(line)
                }
            }
        }

        return closedTagStack.toMap()
    }

    fun getActualTags(taggedSections: Map<String, List<String>>, attribute: String, single: Boolean): List<String> {
        return if (!single) {
            if (attribute == "*") {
                taggedSections.keys.toList()
            } else {
                val requestedTags = attribute.split(",").flatMap { it.split(";") }
                taggedSections.keys.intersect(requestedTags).toList()
            }
        } else {
            taggedSections.keys.intersect(listOf(attribute)).toList()
        }
    }

    fun getTagContent(taggedSections: Map<String, List<String>>, tags: List<String>): String {
        return tags
            .flatMap { taggedSections[it]!! }
            .joinToString(separator = "\n")
    }

    companion object {
        val tagStartRegex = """^.*?tag::(.+?)\[].*?${'$'}""".toRegex()
        val tagEndRegex = """^.*?end::(.+?)\[].*?${'$'}""".toRegex()
    }
}
