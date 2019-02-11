package com.eden.orchid.groovydoc.pages

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.groovydoc.GroovydocGenerator
import com.eden.orchid.groovydoc.resources.BaseGroovydocResource

@Archetype(value = ConfigArchetype::class, key = "${GroovydocGenerator.GENERATOR_KEY}.pages")
abstract class BaseGroovydocPage(
        resource: BaseGroovydocResource,
        key: String,
        title: String
) : OrchidPage(resource, key, title) {

}
