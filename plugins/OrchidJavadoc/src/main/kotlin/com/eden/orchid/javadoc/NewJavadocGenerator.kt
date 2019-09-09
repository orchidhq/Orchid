package com.eden.orchid.javadoc

import com.copperleaf.kodiak.java.JavadocInvokerImpl
import com.copperleaf.kodiak.java.models.JavaRootDoc
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.permalinks.PermalinkStrategy
import com.eden.orchid.javadoc.models.JavaDocModuleConfig
import com.eden.orchid.sourcedoc.SourcedocGenerator
import javax.inject.Inject
import javax.inject.Named

class NewJavadocGenerator
@Inject
constructor(
    @Named("src") resourcesDir: String,
    invoker: JavadocInvokerImpl,
    extractor: OptionsExtractor,
    permalinkStrategy: PermalinkStrategy
) : SourcedocGenerator<JavaRootDoc, JavaDocModuleConfig>(
    GENERATOR_KEY,
    resourcesDir,
    invoker,
    extractor,
    permalinkStrategy
) {

    @Option
    override lateinit var modules: MutableList<JavaDocModuleConfig>

    @Option
    @Description("The configuration for the default wiki, when no other categories are set up.")
    override lateinit var defaultConfig: JavaDocModuleConfig

    companion object {
        const val GENERATOR_KEY = "javadoc"

        val type = "java"
        val nodeKinds = listOf("packages", "classes")
        val otherSourceKinds = emptyList<String>()
    }
}
