package com.eden.orchid.swiftdoc

import com.copperleaf.kodiak.swift.SwiftdocInvokerImpl
import com.copperleaf.kodiak.swift.models.SwiftModuleDoc
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.api.theme.permalinks.PermalinkStrategy
import com.eden.orchid.sourcedoc.SourcedocGenerator
import javax.inject.Inject

class NewSwiftdocGenerator
@Inject
constructor(
    @javax.inject.Named("src") resourcesDir: String,
    invoker: SwiftdocInvokerImpl,
    extractor: OptionsExtractor,
    permalinkStrategy: PermalinkStrategy
) : SourcedocGenerator<SwiftModuleDoc>("swiftdoc", resourcesDir, invoker, extractor, permalinkStrategy) {
    companion object {
        val type = "swift"
        val nodeKinds = listOf("sourceFiles", "classes")
        val otherSourceKinds = emptyList<String>()
    }
}
