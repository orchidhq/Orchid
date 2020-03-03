package com.eden.orchid.sourcedoc.page

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.render.RenderService
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.sourcedoc.options.SourcedocPageConfigArchetype
import com.eden.orchid.utilities.extractOptionsFromResource

@Archetype(value = SourcedocPageConfigArchetype::class, key = "pages")
abstract class BaseSourceDocPage(
    resource: OrchidResource,
    key: String,
    title: String,
    val moduleType: String,
    val moduleGroup: String,
    val module: String,
    val moduleSlug: String
) : OrchidPage(
    resource,
    RenderService.RenderMode.TEMPLATE,
    key,
    title
) {

    init {
        data = extractOptionsFromResource(context, resource)
        postInitialize(title)
    }

    override fun initialize(title: String?) {

    }
}
