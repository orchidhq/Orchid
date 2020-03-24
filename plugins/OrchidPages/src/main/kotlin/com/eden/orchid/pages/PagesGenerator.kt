package com.eden.orchid.pages

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.FileCollection
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.generators.modelOf
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.pages.pages.StaticPage
import com.eden.orchid.utilities.OrchidUtils

@Description(
    "Generates static pages with the same output folder as their input, minus the base directory. Input " +
            "pages come from 'baseDir' option value, which defaults to 'pages'.",
    name = "Static Pages"
)
class PagesGenerator : OrchidGenerator<OrchidGenerator.Model>(GENERATOR_KEY, Stage.CONTENT) {

    companion object {
        const val GENERATOR_KEY = "pages"
    }

    @Option
    @StringDefault("pages")
    @Description("The base directory in local resources to look for static pages in.")
    lateinit var baseDir: String

    override fun startIndexing(context: OrchidContext): Model {
        val resourcesList = context.getResourceEntries(baseDir, null, true, LocalResourceSource)
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

        for (page in pages) {
            var parentPath = getPathWithFilename(page)

            parentPath = parentPath.split("/").toList().dropLast(1).joinToString("/")

            while (parentPath.isNotBlank()) {
                parentPath = parentPath.split("/").toList().dropLast(1).joinToString("/")

                if (pagesMap.containsKey(parentPath)) {
                    page.parent = pagesMap[parentPath]
                    break
                }
            }
        }

        val collections = getCollections(pages)
        return modelOf({ pages }, { collections })
    }

    override fun startGeneration(context: OrchidContext, model: Model) {
        model
            .allPages
            .filterIsInstance<StaticPage>()
            .forEach {
                context.renderPageWithTheme(it, this@PagesGenerator.theme, it.theme) { page ->
                    context.render(page)
                }
            }
    }

    private fun getCollections(
        allPages: List<StaticPage>
    ): List<OrchidCollection<*>> {
        val pageGroupMap = HashMap<String?, MutableList<OrchidPage>>()
        val collections = ArrayList<OrchidCollection<*>>()

        for (page in allPages) {
            pageGroupMap.getOrPut(page.group, { ArrayList() }).add(page)
        }

        pageGroupMap.forEach { group, groupPages ->
            if (group != null) {
                collections.add(StaticPageGroupCollection(this, group, groupPages))
            }
        }

        val allPagesCollection = FileCollection(this, "allPages", allPages)
        collections.add(allPagesCollection)

        return collections
    }

    private fun getPathWithFilename(page: StaticPage): String {
        val outputPath = OrchidUtils.normalizePath(page.reference.path)
        val outputName: String?
        if (EdenUtils.isEmpty(OrchidUtils.normalizePath(page.reference.outputExtension))) {
            outputName = OrchidUtils.normalizePath(page.reference.fileName)
        } else {
            outputName = OrchidUtils.normalizePath(page.reference.fileName)
        }

        return "$outputPath/$outputName"
    }
}
