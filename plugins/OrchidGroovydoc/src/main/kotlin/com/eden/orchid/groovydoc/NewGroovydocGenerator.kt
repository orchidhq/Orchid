package com.eden.orchid.groovydoc

import com.copperleaf.kodiak.groovy.GroovydocInvokerImpl
import com.copperleaf.kodiak.groovy.models.GroovyModuleDoc
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.permalinks.PermalinkStrategy
import com.eden.orchid.groovydoc.models.GroovyDocModuleConfig
import com.eden.orchid.sourcedoc.SourcedocGenerator
import javax.inject.Inject
import javax.inject.Named

class NewGroovydocGenerator
@Inject
constructor(
    @Named("src") resourcesDir: String,
    invoker: GroovydocInvokerImpl,
    extractor: OptionsExtractor,
    permalinkStrategy: PermalinkStrategy
) : SourcedocGenerator<GroovyModuleDoc, GroovyDocModuleConfig>(
    GENERATOR_KEY,
    resourcesDir,
    invoker,
    extractor,
    permalinkStrategy
) {

    @Option
    override lateinit var modules: MutableList<GroovyDocModuleConfig>

    @Option
    @Description("The configuration for the default wiki, when no other categories are set up.")
    override lateinit var defaultConfig: GroovyDocModuleConfig

    companion object {
        const val GENERATOR_KEY = "groovydoc"

        val type = "groovy"
        val nodeKinds = listOf("packages", "classes")
        val otherSourceKinds = listOf("java")
    }
}
