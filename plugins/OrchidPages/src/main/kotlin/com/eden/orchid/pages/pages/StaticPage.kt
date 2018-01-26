package com.eden.orchid.pages.pages

import com.eden.orchid.api.options.annotations.*
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.pages.PageGroupArchetype
import com.eden.orchid.utilities.from
import com.eden.orchid.utilities.snakeCase
import com.eden.orchid.utilities.to
import com.eden.orchid.utilities.words

@Archetypes(
        Archetype(value = ConfigArchetype::class, key = "staticPages"),
        Archetype(value = PageGroupArchetype::class, key = "staticPages")
)
class StaticPage(resource: OrchidResource)
    : OrchidPage(resource, "page", resource.reference.title.from { snakeCase { capitalize() } }.to { words() }) {

    @Option @BooleanDefault(true)
    var usePrettyUrl: Boolean = true

    @Option @StringDefault("template")
    lateinit var renderMode: String

    override fun onPostExtraction() {
        reference.isUsePrettyUrl = usePrettyUrl
    }

    val group: String?
        get() {
            if(reference.pathSegments.size > 1) {
                return reference.getPathSegment(0)
            }

            return null
        }
}

