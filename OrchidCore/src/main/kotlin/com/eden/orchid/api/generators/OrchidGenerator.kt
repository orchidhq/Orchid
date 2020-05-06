package com.eden.orchid.api.generators

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.AllOptions
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.registration.Prioritized
import com.eden.orchid.api.server.annotations.ImportantModularType
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.impl.relations.ThemeRelation
import javax.inject.Inject

@ImportantModularType
@Description(value = "A plugin that creates a new content model in your site.", name = "Generators")
@Archetype(value = ConfigArchetype::class, key = "allGenerators")
abstract class OrchidGenerator<T : OrchidGenerator.Model>
@Inject
constructor(
    val key: String,
    val stage: Stage
) : Prioritized(stage.priority), OptionsHolder {

    @Option
    @Description(
        "Set a theme to be used only when rendering pages from this Generator. This can be a String to use " +
                "that theme's default options set in `config.yml`, or an object with a `key` property to use those " +
                "specific options for the theme."
    )
    lateinit var theme: ThemeRelation

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
    open fun startGeneration(context: OrchidContext, model: T) {
        model.allPages.forEach { context.render(it) }
    }

    companion object {
        @Deprecated("Use Stage.WARM_UP directly instead", ReplaceWith("Stage.WARM_UP"))
        val PRIORITY_INIT = Stage.WARM_UP

        @Deprecated("Use Stage.CONTENT directly instead", ReplaceWith("Stage.CONTENT"))
        val PRIORITY_EARLY = Stage.CONTENT

        @Deprecated("Use Stage.COLLECTION directly instead", ReplaceWith("Stage.COLLECTION"))
        val PRIORITY_DEFAULT = Stage.COLLECTION

        @Deprecated("Use Stage.META directly instead", ReplaceWith("Stage.META"))
        val PRIORITY_LATE = Stage.META
    }

    enum class Stage(val priority: Int) {
        /**
         * A Stage for Generators that produce pages that Content pages depend on, like registering global assets and
         * warming up other caches which improve overall build performance.
         */
        WARM_UP(10000),

        /**
         * A Stage for Generators that produce content pages. These are pages that are generally standalone pages, only
         * have relationships to other pages from the same Generator.
         */
        CONTENT(1000),

        /**
         * A Stage for Generators that collect Content pages into groups, taxonomies, etc, and optionally generate
         * additional Content pages displaying those collections of pages.
         */
        COLLECTION(100),

        /**
         * A Stage for Generators that produce metadata about your site. These are not Content pages, and are usually
         * intended for _computers_ to read, not humans, such as sitemaps, search indices, etc.
         */
        META(10);
    }

    interface Model {
        val allPages: List<OrchidPage>
        val collections: List<OrchidCollection<*>>
    }

    class SimpleModel(
        override val allPages: List<OrchidPage>,
        override val collections: List<OrchidCollection<*>>
    ) : Model
}

fun OrchidGenerator<*>.modelOf(
    indexedPages: () -> List<OrchidPage>,
    indexedCollections: OrchidGenerator<*>.(List<OrchidPage>) -> List<OrchidCollection<*>>
): OrchidGenerator.Model {
    val pages = indexedPages()
    val collections = indexedCollections(pages)

    return OrchidGenerator.SimpleModel(indexedPages(), collections)
}

fun OrchidGenerator<*>.modelOf(
    indexedPages: () -> List<OrchidPage>
): OrchidGenerator.Model {
    return modelOf(indexedPages = indexedPages, indexedCollections = { simplePageCollection(it) })
}

private fun OrchidGenerator<*>.simplePageCollection(pages: List<OrchidPage>): List<OrchidCollection<*>> {
    return if (pages.isNotEmpty()) {
        listOf(
            FileCollection(
                this,
                this.key,
                pages
            )
        )
    } else {
        emptyList()
    }
}

fun OrchidGenerator<*>.emptyModel(): OrchidGenerator.Model {
    return modelOf { emptyList() }
}
