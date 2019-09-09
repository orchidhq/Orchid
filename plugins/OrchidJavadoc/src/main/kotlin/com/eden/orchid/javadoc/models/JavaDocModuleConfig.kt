package com.eden.orchid.javadoc.models

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.javadoc.NewJavadocGenerator
import com.eden.orchid.sourcedoc.model.SourceDocModuleConfig
import javax.inject.Inject
import javax.inject.Named

@Archetype(value = ConfigArchetype::class, key = NewJavadocGenerator.GENERATOR_KEY)
class JavaDocModuleConfig
@Inject
constructor(
    @Named("javadocClasspath") private val javadocClasspath: String
) : SourceDocModuleConfig() {

    override fun additionalRunnerArgs(): List<String> = if (javadocClasspath.isNotBlank()) listOf(
        "-classpath",
        javadocClasspath,
        *args.toTypedArray()
    ) else args
}
