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
import javax.inject.Named
import javax.inject.Provider

class AsciidocIncludeProcessor
@Inject
constructor(
    private var contextProvider: Provider<OrchidContext>,
    @Named("src") private val resourcesDir: String
) : IncludeProcessor() {

    private val context by lazy { contextProvider.get() }

    override fun process(
        document: Document?,
        reader: PreprocessorReader?,
        target: String?,
        attributes: MutableMap<String, Any>?
    ) {
        val resource = context.getResourceEntry(target, LocalResourceSource)

        if(resource != null) {
            reader?.push_include(
                resource.getContentStream().readToString(),
                target,
                resource.reference.originalFullFileName,
                1,
                attributes
            )
        }
    }

    override fun handles(target: String?): Boolean {
        // if there's a file relative to the base dir, don't handle it here. Let Asciidoctor do that, which preserves
        // all the attrs it can normally handle. Only if a file cannot be found, should we attempt to handle it, and
        // then we can check Orchid resources as a fallback

        return when {
            OrchidUtils.isExternal(target) -> false
            File(resourcesDir).resolve(target ?: "").exists() -> false
            else -> true
        }
    }
}
