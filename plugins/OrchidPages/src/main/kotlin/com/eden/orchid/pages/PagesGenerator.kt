package com.eden.orchid.pages

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.FileCollection
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.pages.pages.StaticPage
import com.eden.orchid.utilities.OrchidUtils
import java.util.stream.Stream
import javax.inject.Inject
import kotlin.streams.toList

@Description("Generates static pages with the same output folder as their input, minus the base directory. Input " +
        "pages come from 'baseDir' option value, which defaults to 'pages'.",
        name = "Static Pages"
)
class PagesGenerator
@Inject
constructor(
        context: OrchidContext
) : OrchidGenerator(context, GENERATOR_KEY, OrchidGenerator.PRIORITY_EARLY) {

    companion object {
        const val GENERATOR_KEY = "pages"
    }

    @Option
    @StringDefault("pages")
    @Description("The base directory in local resources to look for static pages in.")
    lateinit var baseDir: String

    override fun startIndexing(): List<OrchidPage> {
        val resourcesList = context.getLocalResourceEntries(baseDir, null, true)
        val pages = ArrayList<StaticPage>()

        val pagesMap = HashMap<String, StaticPage>()

        for (entry in resourcesList) {
            entry.reference.stripFromPath(baseDir)
            if (entry.reference.originalFileName.equals("index", ignoreCase = true)) {
                entry.reference.setAsDirectoryIndex()
            }

            val page = StaticPage(entry)
            pages.add(page)

            pagesMap[getPathWithFilename(page)] = page
        }

        for(page in pages) {
            var parentPath = getPathWithFilename(page)

            parentPath = parentPath.split("/").toList().dropLast(1).joinToString("/")

            while(parentPath.isNotBlank()) {
                parentPath = parentPath.split("/").toList().dropLast(1).joinToString("/")

                if(pagesMap.containsKey(parentPath)) {
                    page.parent = pagesMap[parentPath]
                    break
                }
            }
        }

        return pages
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        val pagesList = pages.toList()

        val usesCustomThemes = pagesList.stream().anyMatch { it is StaticPage && it.theme.get() != null }

        val stream = if (usesCustomThemes) {
            pagesList.stream().sequential()
        }
        else {
            pagesList.stream()
        }

        stream.forEach { page ->
            if (page is StaticPage) {
                context.doWithTheme(page.theme) { context.render(page, page.renderMode) }
            }
        }
    }

    override fun getCollections(pages: List<OrchidPage>): List<OrchidCollection<*>> {
        val pageGroupMap = HashMap<String?, MutableList<OrchidPage>>()
        val collections = ArrayList<OrchidCollection<*>>()

        for (page in pages) {
            if (page is StaticPage) {
                pageGroupMap.getOrPut(page.group, { ArrayList() }).add(page)
            }
        }

        pageGroupMap.forEach { group, groupPages ->
            if (group != null) {
                collections.add(StaticPageGroupCollection(this, group, groupPages))
            }
        }

        val allPagesCollection = FileCollection(this, "allPages", pages)
        collections.add(allPagesCollection)

        return collections
    }

    private fun getPathWithFilename(page: StaticPage): String {
        val outputPath = OrchidUtils.normalizePath(page.reference.path)
        val outputName: String?
        if (EdenUtils.isEmpty(OrchidUtils.normalizePath(page.reference.outputExtension))) {
            outputName = OrchidUtils.normalizePath(page.reference.fileName)
        }
        else {
            outputName = OrchidUtils.normalizePath(page.reference.fileName)
        }

        return "$outputPath/$outputName"
    }
}
