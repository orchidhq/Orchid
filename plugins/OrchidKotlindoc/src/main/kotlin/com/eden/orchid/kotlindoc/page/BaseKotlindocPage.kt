@file:Suppress(SuppressedWarnings.DEPRECATION)
package com.eden.orchid.kotlindoc.page

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.render.RenderService
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.kotlindoc.KotlindocGenerator
import com.eden.orchid.kotlindoc.resources.BaseKotlindocResource
import com.eden.orchid.utilities.SuppressedWarnings

@Archetype(value = ConfigArchetype::class, key = "${KotlindocGenerator.GENERATOR_KEY}.pages")
abstract class BaseKotlindocPage(
        resource: BaseKotlindocResource,
        key: String,
        title: String
) : OrchidPage(resource, RenderService.RenderMode.TEMPLATE, key, title)
