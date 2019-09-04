package com.eden.orchid.groovydoc

import com.copperleaf.kodiak.groovy.GroovydocInvokerImpl
import com.copperleaf.kodiak.groovy.models.GroovyModuleDoc
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.sourcedoc.SourcedocGenerator
import javax.inject.Inject
import javax.inject.Named

class NewGroovydocGenerator
@Inject
constructor(
    @Named("src") resourcesDir: String,
    invoker: GroovydocInvokerImpl,
    extractor: OptionsExtractor
) : SourcedocGenerator<GroovyModuleDoc>("groovydoc", resourcesDir, invoker, extractor) {
    companion object {
        val type = "groovy"
        val nodeKinds = listOf("packages", "classes")
        val otherSourceKinds = listOf("java")
    }
}
