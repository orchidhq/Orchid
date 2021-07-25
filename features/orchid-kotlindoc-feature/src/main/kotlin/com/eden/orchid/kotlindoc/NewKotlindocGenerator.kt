package com.eden.orchid.kotlindoc

import com.copperleaf.kodiak.kotlin.KotlindocInvokerImpl
import com.copperleaf.kodiak.kotlin.models.KotlinModuleDoc
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.permalinks.PermalinkStrategy
import com.eden.orchid.kotlindoc.model.KotlinDocModuleConfig
import com.eden.orchid.sourcedoc.SourcedocGenerator
import javax.inject.Inject

@Description(
    "Generate SourceDoc content for Kotlin/Java source files",
    name = "Kotlindoc"
)
class NewKotlindocGenerator
@Inject
constructor(
    invoker: KotlindocInvokerImpl,
    extractor: OptionsExtractor,
    permalinkStrategy: PermalinkStrategy
) : SourcedocGenerator<KotlinModuleDoc, KotlinDocModuleConfig>(
    GENERATOR_KEY,
    invoker,
    extractor,
    permalinkStrategy
) {

    @Option
    override lateinit var modules: MutableList<KotlinDocModuleConfig>

    @Option
    @Description("The configuration for the default wiki, when no other categories are set up.")
    override lateinit var defaultConfig: KotlinDocModuleConfig

    companion object {
        const val GENERATOR_KEY = "kotlindoc"

        val type = "kotlin"
        val nodeKinds = listOf("packages", "classes")
        val otherSourceKinds = listOf("java")
    }
}
