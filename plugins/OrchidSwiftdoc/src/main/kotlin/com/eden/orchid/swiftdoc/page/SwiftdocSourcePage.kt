@file:Suppress(SuppressedWarnings.DEPRECATION)
package com.eden.orchid.swiftdoc.page

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.render.RenderService
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.swiftdoc.SwiftdocGenerator
import com.eden.orchid.swiftdoc.swift.SwiftStatement
import com.eden.orchid.utilities.SuppressedWarnings

@Description(value = "A page describing the elements in a Swift source file.", name = "Swift Source")
@Archetype(value = ConfigArchetype::class, key = "${SwiftdocGenerator.GENERATOR_KEY}.pages")
class SwiftdocSourcePage(
        resource: OrchidResource,
        val statements: List<SwiftStatement>,
        val codeJson: String
) : OrchidPage(resource, RenderService.RenderMode.TEMPLATE, "swiftdocSource", null) {


}
