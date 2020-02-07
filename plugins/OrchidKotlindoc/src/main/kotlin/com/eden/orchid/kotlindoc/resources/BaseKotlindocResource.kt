package com.eden.orchid.kotlindoc.resources

import com.copperleaf.dokka.json.models.KotlinDocElement
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidReference

abstract class BaseKotlindocResource(
        context: OrchidContext,
        qualifiedName: String,
        var doc: KotlinDocElement
) : OrchidResource(OrchidReference(context, qualifiedName.replace("\\.".toRegex(), "/") + ".html")) {

    init {
        reference.extension = "md"
    }
}
