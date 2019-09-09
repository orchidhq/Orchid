package com.eden.orchid.swiftdoc

import com.copperleaf.kodiak.swift.SwiftdocInvokerImpl
import com.copperleaf.kodiak.swift.models.SwiftModuleDoc
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.theme.permalinks.PermalinkStrategy
import com.eden.orchid.sourcedoc.SourcedocGenerator
import com.eden.orchid.swiftdoc.NewSwiftdocGenerator.Companion.GENERATOR_KEY
import com.eden.orchid.swiftdoc.model.SwiftDocModuleConfig
import javax.inject.Inject

@Archetype(value = ConfigArchetype::class, key = GENERATOR_KEY)
class NewSwiftdocGenerator
@Inject
constructor(
    @javax.inject.Named("src") resourcesDir: String,
    invoker: SwiftdocInvokerImpl,
    extractor: OptionsExtractor,
    permalinkStrategy: PermalinkStrategy
) : SourcedocGenerator<SwiftModuleDoc, SwiftDocModuleConfig>(
    GENERATOR_KEY,
    resourcesDir,
    invoker,
    extractor,
    permalinkStrategy
) {

    @Option
    override lateinit var modules: MutableList<SwiftDocModuleConfig>

    @Option
    @Description("The configuration for the default wiki, when no other categories are set up.")
    override lateinit var defaultConfig: SwiftDocModuleConfig

    companion object {
        const val GENERATOR_KEY = "swiftdoc"

        val type = "swift"
        val nodeKinds = listOf("sourceFiles", "classes")
        val otherSourceKinds = emptyList<String>()
    }
}
