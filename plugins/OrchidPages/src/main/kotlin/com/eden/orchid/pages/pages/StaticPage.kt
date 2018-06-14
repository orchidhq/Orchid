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
import com.eden.orchid.impl.relations.PageRelation
import com.eden.orchid.pages.PageGroupArchetype
import com.eden.orchid.pages.PagesGenerator

@Archetypes(
        Archetype(value = ConfigArchetype::class, key = "${PagesGenerator.GENERATOR_KEY}.staticPages"),
        Archetype(value = PageGroupArchetype::class, key = "${PagesGenerator.GENERATOR_KEY}.staticPages")
)
class StaticPage(resource: OrchidResource)
    : OrchidPage(resource, "staticPage") {

    @Option @BooleanDefault(true)
    @Description("Whether to use the 'pretty' URL version when linking to this page or not.")
    var usePrettyUrl: Boolean = true

    @Option @StringDefault("template")
    @Description("How should this page be rendered? One of [TEMPLATE, RAW, or BINARY].")
    lateinit var renderMode: String

    @Option
    @Description("Set a theme to be used only when rendering pages this Static Page. This can be a String to use " +
            "that theme's default options set in `config.yml`, or an object with a `key` property to use those " +
            "specific options for the theme."
    )
    var theme: Any? = null

    @Option("next")
    @Description("The reference to the next page to link to.")
    var nextPage: PageRelation? = null
    fun setNext(nextPage: PageRelation?) {
        this.nextPage = nextPage
    }
    override fun getNext(): OrchidPage? {
        return if (nextPage != null && nextPage!!.get() != null) {
            nextPage!!.get()
        }
        else super.getNext()
    }

    @Option("previous")
    @Description("The reference to the previous page to link to.")
    var previousPage: PageRelation? = null
    fun setPrevious(previousPage: PageRelation?) {
        this.previousPage = previousPage
    }
    override fun getPrevious(): OrchidPage? {
        return if (previousPage != null && previousPage!!.get() != null) {
            previousPage!!.get()
        }
        else super.getPrevious()
    }

    @Option("parent")
    @Description("The reference to the parent page to link to.")
    var parentPage: PageRelation? = null
    fun setParent(parentPage: PageRelation?) {
        this.parentPage = parentPage
    }
    override fun getParent(): OrchidPage? {
        return if (parentPage != null && parentPage!!.get() != null) {
            parentPage!!.get()
        }
        else super.getParent()
    }

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
            templates.add("$key-${group!!}")
        }

        return templates
    }

}

