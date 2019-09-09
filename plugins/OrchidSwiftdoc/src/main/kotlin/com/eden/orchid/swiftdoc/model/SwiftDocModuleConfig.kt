package com.eden.orchid.swiftdoc.model

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.sourcedoc.model.SourceDocModuleConfig
import com.eden.orchid.swiftdoc.NewSwiftdocGenerator

@Archetype(value = ConfigArchetype::class, key = NewSwiftdocGenerator.GENERATOR_KEY)
class SwiftDocModuleConfig : SourceDocModuleConfig()
