package com.eden.orchid.kotlindoc

import com.copperleaf.kodiak.kotlin.KotlindocInvokerImpl
import com.copperleaf.kodiak.kotlin.models.KotlinModuleDoc
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.sourcedoc.SourcedocGenerator
import javax.inject.Inject
import javax.inject.Named

class NewKotlindocGenerator
@Inject
constructor(
    @Named("src") resourcesDir: String,
    invoker: KotlindocInvokerImpl,
    extractor: OptionsExtractor
) : SourcedocGenerator<KotlinModuleDoc>("kotlindoc", resourcesDir, invoker, extractor) {
    companion object {
        val type = "kotlin"
        val nodeKinds = listOf("packages", "classes")
        val otherSourceKinds = listOf("java")
    }
}
