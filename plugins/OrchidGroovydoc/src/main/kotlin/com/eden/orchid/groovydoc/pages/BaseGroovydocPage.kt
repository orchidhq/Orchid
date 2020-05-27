@file:Suppress(SuppressedWarnings.DEPRECATION)
package com.eden.orchid.groovydoc.pages

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.render.RenderService
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.groovydoc.GroovydocGenerator
import com.eden.orchid.groovydoc.resources.BaseGroovydocResource
import com.eden.orchid.utilities.SuppressedWarnings

@Archetype(value = ConfigArchetype::class, key = "${GroovydocGenerator.GENERATOR_KEY}.pages")
abstract class BaseGroovydocPage(
        resource: BaseGroovydocResource,
        key: String,
        title: String
) : OrchidPage(resource, RenderService.RenderMode.TEMPLATE, key, title) {

}
