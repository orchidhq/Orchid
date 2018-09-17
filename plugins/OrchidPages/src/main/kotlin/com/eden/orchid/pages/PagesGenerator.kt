package com.eden.orchid.pages

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.FileCollection
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.pages.pages.StaticPage
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.streams.toList

@Singleton
@Description("Generates static pages with the same output folder as their input, minus the base directory. Input " +
        "pages come from 'baseDir' option value, which defaults to 'pages'."
)
class PagesGenerator @Inject
constructor(context: OrchidContext) : OrchidGenerator(context, GENERATOR_KEY, OrchidGenerator.PRIORITY_EARLY) {

    companion object {
        const val GENERATOR_KEY = "pages"
    }

    @Option @StringDefault("pages")
    @Description("The base directory in local resources to look for static pages in.")
    lateinit var baseDir: String

    override fun startIndexing(): List<OrchidPage> {
        val resourcesList = context.getLocalResourceEntries(baseDir, null, true)
        val pages = ArrayList<StaticPage>()

        for (entry in resourcesList) {
            entry.reference.stripFromPath(baseDir)
            if(entry.reference.originalFileName.equals("index", true)) {
                entry.reference.fileName = entry.reference.originalPathSegments.last()
                entry.reference.removePathSegment(entry.reference.originalPathSegments.lastIndex)
            }

            val page = StaticPage(entry)
            pages.add(page)
        }

        return pages
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        val pagesList = pages.toList()

        val usesCustomThemes = pagesList.stream().anyMatch { it is StaticPage && it.theme != null }

        val stream = if(usesCustomThemes) { pagesList.stream().sequential() } else { pagesList.stream() }

        stream.forEach { page -> if (page is StaticPage) {
            context.doWithTheme(page.theme) { context.render(page, page.renderMode) }
        } }
    }

    override fun getCollections(): List<OrchidCollection<*>> {
        val ownPages = context.internalIndex.getChildIndex(this.key)
        val pageGroupMap = HashMap<String?, MutableList<OrchidPage>>()
        val collections = ArrayList<OrchidCollection<*>>()

        for (page in ownPages) {
            if(page is StaticPage) {
                pageGroupMap.getOrPut(page.group, {ArrayList()}).add(page)
            }
        }

        pageGroupMap.forEach { group, pages ->
            if(group != null) {
                collections.add(StaticPageGroupCollection(this, group, pages))
            }
        }

        val allPagesCollection = FileCollection(this, "allPages", ownPages)
        collections.add(allPagesCollection)

        return collections
    }
}
