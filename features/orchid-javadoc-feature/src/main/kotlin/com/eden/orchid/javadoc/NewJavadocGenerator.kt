package com.eden.orchid.javadoc

import com.copperleaf.kodiak.java.JavadocInvokerImpl
import com.copperleaf.kodiak.java.models.JavaModuleDoc
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.permalinks.PermalinkStrategy
import com.eden.orchid.javadoc.models.JavaDocModuleConfig
import com.eden.orchid.sourcedoc.SourcedocGenerator
import javax.inject.Inject

@Description(
    "Generate SourceDoc content for Java source files",
    name = "Javadoc"
)
class NewJavadocGenerator
@Inject
constructor(
    invoker: JavadocInvokerImpl,
    extractor: OptionsExtractor,
    permalinkStrategy: PermalinkStrategy
) : SourcedocGenerator<JavaModuleDoc, JavaDocModuleConfig>(
    GENERATOR_KEY,
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
