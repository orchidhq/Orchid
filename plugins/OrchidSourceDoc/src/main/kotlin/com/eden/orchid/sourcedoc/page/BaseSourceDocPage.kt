package com.eden.orchid.sourcedoc.page

import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage

abstract class BaseSourceDocPage(
    resource: OrchidResource,
    key: String,
    title: String,
    val moduleType: String,
    val module: String
) : OrchidPage(
    resource,
    key,
    title
)
