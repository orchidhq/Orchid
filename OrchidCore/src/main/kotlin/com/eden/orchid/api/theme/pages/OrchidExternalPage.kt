package com.eden.orchid.api.theme.pages

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.resources.resource.ExternalResource
import com.eden.orchid.api.resources.resource.OrchidResource

/**
 * An OrchidExternalPage is little more than an OrchidReference which points to a page on an external site. These pages
 * are intended just to be used so external pages can be linked to in the same way as internal pages.
 */
@Description(
    value = "A page referencing a URL outside of your current build. External assets may be downloaded and " + "hosted in your own site for robustness.",
    name = "External Page"
)
class OrchidExternalPage : OrchidPage {

    override val itemIds: List<String> = listOf(
        reference.path,
        reference.path.replace('/', '.'),
        title
    )

    constructor(reference: OrchidReference) : super(ExternalResource(reference), "ext", reference.title)

    constructor(resource: OrchidResource, key: String, title: String) : super(resource, key, title)
}
