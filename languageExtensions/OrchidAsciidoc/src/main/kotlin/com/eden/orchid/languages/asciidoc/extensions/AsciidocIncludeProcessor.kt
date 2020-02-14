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
        val resolvedResource = context.getResourceEntry(target, LocalResourceSource)
        if(resolvedResource != null) {
            handleIncludeContent(
                document,
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
        if(docDir != null) {
            val baseDir = File(docDir)
            val resolvedFile = baseDir.resolve(target ?: "")

            if(resolvedFile.exists()) {
                handleIncludeContent(
                    document,
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
        document: Document?,
        reader: PreprocessorReader?,
        target: String?,
        attributes: MutableMap<String, Any>?,
        resolvedPath: String?,
        resolvedContent: String
    ) {
        // TODO: preprocess resolvedContent to handle the standard AsciiDoc include tags (because the stupid library
        //   doesn't handle those for custom IncludeProcessor)

        reader?.push_include(
            resolvedContent,
            target,
            resolvedPath,
            1,
            attributes
        )
    }

    private fun getActualLines(attributes: MutableMap<String, Any>?) : List<Int> {
        return emptyList()
    }

    private fun getTaggedSections(resolvedContent: String) : Map<String, String> {
        return emptyMap()
    }

    override fun handles(target: String?): Boolean {
        // if there's a file relative to the base dir, don't handle it here. Let Asciidoctor do that, which preserves
        // all the attrs it can normally handle. Only if a file cannot be found, should we attempt to handle it, and
        // then we can check Orchid resources as a fallback

        return false
//        return when {
//            OrchidUtils.isExternal(target) -> false
//            else -> true
//        }
    }
}
