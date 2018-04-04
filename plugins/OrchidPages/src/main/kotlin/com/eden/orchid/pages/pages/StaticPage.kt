package com.eden.orchid.pages.pages

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Archetypes
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.pages.PageGroupArchetype
import com.eden.orchid.pages.PagesGenerator
import com.eden.orchid.utilities.from
import com.eden.orchid.utilities.snakeCase
import com.eden.orchid.utilities.to
import com.eden.orchid.utilities.words

@Archetypes(
        Archetype(value = ConfigArchetype::class, key = "${PagesGenerator.GENERATOR_KEY}.staticPages"),
        Archetype(value = PageGroupArchetype::class, key = "${PagesGenerator.GENERATOR_KEY}.staticPages")
)
class StaticPage(resource: OrchidResource)
    : OrchidPage(resource, "staticPage", resource.reference.title.from { snakeCase { capitalize() } }.to { words() }) {

    @Option @BooleanDefault(true)
    @Description("Whether to use the 'pretty' URL version when linking to this page or not.")
    var usePrettyUrl: Boolean = true

    @Option @StringDefault("template")
    @Description("How should this page be rendered? One of [TEMPLATE, RAW, or BINARY].")
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

    override fun getTemplates(): List<String> {
        val templates = super.getTemplates()
        if (!EdenUtils.isEmpty(group)) {
            templates.add(0, "$key-${group!!}")
        }

        return templates
    }
}

