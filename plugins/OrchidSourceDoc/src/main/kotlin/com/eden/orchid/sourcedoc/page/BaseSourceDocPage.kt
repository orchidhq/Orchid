package com.eden.orchid.sourcedoc.page

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.sourcedoc.options.SourcedocPageConfigArchetype

@Archetype(value = SourcedocPageConfigArchetype::class, key = "pages")
abstract class BaseSourceDocPage(
    resource: OrchidResource,
    key: String,
    title: String,
    val moduleType: String,
    val moduleGroup: String,
    val module: String
) : OrchidPage(
    resource,
    key,
    title
) {

    init {
        this.extractOptions(this.context, data)
        postInitialize(title)
    }

    override fun initialize(title: String?) {

    }
}
