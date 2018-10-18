package com.eden.orchid.javadoc.pages

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.javadoc.JavadocGenerator
import com.eden.orchid.javadoc.resources.BaseJavadocResource

@Archetype(value = ConfigArchetype::class, key = "${JavadocGenerator.GENERATOR_KEY}.pages")
abstract class BaseJavadocPage(
        resource: BaseJavadocResource,
        key: String,
        title: String
) : OrchidPage(resource, key, title) {

}
