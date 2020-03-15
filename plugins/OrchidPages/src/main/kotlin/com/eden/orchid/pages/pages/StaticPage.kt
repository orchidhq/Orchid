package com.eden.orchid.pages.pages

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Archetypes
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.render.RenderService
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.impl.relations.ThemeRelation
import com.eden.orchid.pages.PageGroupArchetype
import com.eden.orchid.pages.PagesGenerator
import java.lang.Exception

@Archetypes(
        Archetype(value = PageGroupArchetype::class, key = PagesGenerator.GENERATOR_KEY),
        Archetype(value = ConfigArchetype::class, key = "${PagesGenerator.GENERATOR_KEY}.staticPages")
)
@Description(value = "A generic static page.", name = "Static Page")
class StaticPage(
    resource: OrchidResource
) : OrchidPage(
    resource,
    RenderService.RenderMode.TEMPLATE,
    "staticPage",
    null
) {

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
        val templates = ArrayList<String>()
        if (!EdenUtils.isEmpty(group)) {
            templates.add("$key-${group!!}")
        }

        return templates
    }

    override fun getPageRenderMode(): RenderService.RenderMode {
        return try {
            RenderService.RenderMode.valueOf(renderMode)
        } catch (e: Exception) {
            super.getPageRenderMode()
        }
    }
}

