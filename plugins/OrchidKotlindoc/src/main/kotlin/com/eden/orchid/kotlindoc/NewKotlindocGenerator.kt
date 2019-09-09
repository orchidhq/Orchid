package com.eden.orchid.kotlindoc

import com.copperleaf.kodiak.kotlin.KotlindocInvokerImpl
import com.copperleaf.kodiak.kotlin.models.KotlinModuleDoc
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.theme.permalinks.PermalinkStrategy
import com.eden.orchid.kotlindoc.NewKotlindocGenerator.Companion.GENERATOR_KEY
import com.eden.orchid.kotlindoc.model.KotlinDocModuleConfig
import com.eden.orchid.sourcedoc.SourcedocGenerator
import javax.inject.Inject
import javax.inject.Named

@Archetype(value = ConfigArchetype::class, key = GENERATOR_KEY)
class NewKotlindocGenerator
@Inject
constructor(
    @Named("src") resourcesDir: String,
    @Named("kotlindocClasspath") private val kotlindocClasspath: String,
    invoker: KotlindocInvokerImpl,
    extractor: OptionsExtractor,
    permalinkStrategy: PermalinkStrategy
) : SourcedocGenerator<KotlinModuleDoc, KotlinDocModuleConfig>(
    GENERATOR_KEY,
    resourcesDir,
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
