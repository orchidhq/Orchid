package com.eden.orchid.javadoc

import com.copperleaf.kodiak.java.JavadocInvokerImpl
import com.copperleaf.kodiak.java.models.JavaRootDoc
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.sourcedoc.SourcedocGenerator
import javax.inject.Inject
import javax.inject.Named

class NewJavadocGenerator
@Inject
constructor(
    @Named("src") resourcesDir: String,
    invoker: JavadocInvokerImpl,
    extractor: OptionsExtractor
) : SourcedocGenerator<JavaRootDoc>("javadoc", resourcesDir, invoker, extractor) {
    companion object {
        val type = "java"
        val nodeKinds = listOf("packages", "classes")
        val otherSourceKinds = emptyList<String>()
    }
}
