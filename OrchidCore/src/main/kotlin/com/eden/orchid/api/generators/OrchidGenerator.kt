package com.eden.orchid.api.generators

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.AllOptions
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.registration.Prioritized
import com.eden.orchid.api.server.annotations.Extensible
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.impl.relations.ThemeRelation
import javax.inject.Inject

/**
 * Generators are what create the output pages within Orchid. Generators are run after all Options have been processed
 * as part of a OrchidTask. When a OrchidTask is chosen that builds the output site, it happens in two phases: the **indexing
 * phase** and the **generation phase**.
 *
 * ### Indexing Phase
 * During the indexing phase, Generators should determine exactly the output pages they intend to generate, returning an
 * index of those pages. This index should consist of a JSONArray or JSONObject, wrapped in a JSONElement. The content
 * of either should be JSONObjects (either as a list or as a hierarchy) which contain at least the following properties:
 *
 * * 'name' - the display name of the page
 * * 'url' - the url of the target page. This should be absolute using the set baseUrl OrchidOption, which is done automatically
 * by OrchidReference
 *
 * This JSONElement is then placed into the root JSONObject under the key specified by `getKey()`. At generation time,
 * this index will be used to create deep-links throughout the site, typically through the page navigation or from
 * manually creating links, either through Javadoc 'see' or 'link' tags, or using an output filter in the main template.
 *
 * It is important that **no pages be written during the Indexing phase**. The sites's index is not completed at this
 * point, so it is likely that navigation among pages will not work as expected.
 *
 * ### Generation Phase
 * During the Generation phase, Generators can start to write their pages. Typically, this step involves picking a
 * template for an OrchidPage and generating a page according to that template. This template should be provided by the
 * Theme, but if the Theme did not implement the desired template, Orchid will attempt to find a suitable fallback.
 *
 * It is also worth noting that templates can be provided by OrchidGenerator extensions apart from a Theme, and when
 * using Pebble as the main template engine, can be injected into the site's content by extending one of the Theme's
 * layouts, which should typically be 'templates/layouts/index.peb'.
 *
 * @since v1.0.0
 * @orchidApi extensible
 */
@Extensible
@Description(value = "A plugin that creates a new content model in your site.", name = "Generators")
@Archetype(value = ConfigArchetype::class, key = "allGenerators")
abstract class OrchidGenerator<T : OrchidGenerator.Model>
@Inject
constructor(
    val key: String,
    priority: Int
) : Prioritized(priority), OptionsHolder {

    @Option
    @Description(
        "Set a theme to be used only when rendering pages from this Generator. This can be a String to use " +
                "that theme's default options set in `config.yml`, or an object with a `key` property to use those " +
                "specific options for the theme."
    )
    var theme: ThemeRelation? = null

    @Option
    @Description("Set the default layout to be used for all Pages from this Generator. Pages can specify their own " + "layouts, which take precedence over the Generator layout.")
    lateinit var layout: String

    @AllOptions
    lateinit var allData: Map<String, Any>

    /**
     * A callback to build the index of content this OrchidGenerator intends to create.
     *
     * @return a list of pages that will be built by this generator
     */
    abstract fun startIndexing(context: OrchidContext): T

    /**
     * A callback to begin generating content. The index is fully built and should not be changed at this time. The
     * list of pages returned by `startIndexing` is passed back in as an argument to the method.
     *
     * @param pages the pages to render
     */
    abstract fun startGeneration(context: OrchidContext, model: T)

    /**
     * Get a list of the collections that are indexed by this Generator.
     *
     * @return the list of OrchidCollections
     */
    open fun getCollections(context: OrchidContext, model: T): List<OrchidCollection<*>>? {
        return if (!EdenUtils.isEmpty(model.allPages)) {
            listOf(FileCollection(this, this.key, model.allPages))
        } else null
    }

    companion object {

        /**
         * Typically used for Generators that produce pages that content pages depend on, like registering global assets.
         */
        val PRIORITY_INIT = 10000

        /**
         * Typically used for Generators that produce content pages.
         */
        val PRIORITY_EARLY = 1000

        /**
         * Typically used for Generators that produce pages based on data contained in content pages or just index content
         * for use in components, menus, or collections.
         */
        val PRIORITY_DEFAULT = 100

        /**
         * Typically used for Generators that produce assets or data files.
         */
        val PRIORITY_LATE = 10
    }

    interface Model {
        val allPages: List<OrchidPage>
    }

    class SimpleModel(
        override val allPages: List<OrchidPage>
    ) : Model
}

fun OrchidGenerator<*>.modelOf(indexedPages: ()->List<OrchidPage>) : OrchidGenerator.Model {
    return OrchidGenerator.SimpleModel(indexedPages())
}

fun OrchidGenerator<*>.emptyModel() : OrchidGenerator.Model {
    return modelOf { emptyList() }
}