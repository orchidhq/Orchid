package com.eden.orchid.groovydoc.models

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.groovydoc.NewGroovydocGenerator
import com.eden.orchid.sourcedoc.model.SourceDocModuleConfig

@Archetype(value = ConfigArchetype::class, key = NewGroovydocGenerator.GENERATOR_KEY)
class GroovyDocModuleConfig : SourceDocModuleConfig()
