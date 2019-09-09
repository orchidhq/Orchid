package com.eden.orchid.kotlindoc.model

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.kotlindoc.NewKotlindocGenerator
import com.eden.orchid.sourcedoc.model.SourceDocModuleConfig
import javax.inject.Inject
import javax.inject.Named

@Archetype(value = ConfigArchetype::class, key = NewKotlindocGenerator.GENERATOR_KEY)
class KotlinDocModuleConfig
@Inject
constructor(
    @Named("kotlindocClasspath") private val kotlindocClasspath: String
) : SourceDocModuleConfig() {

    override fun additionalRunnerArgs(): List<String> = if (kotlindocClasspath.isNotBlank()) listOf(
        "-classpath",
        kotlindocClasspath,
        *args.toTypedArray()
    ) else args
}
